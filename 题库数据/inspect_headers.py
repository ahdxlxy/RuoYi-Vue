import pandas as pd
import json
import glob
import datetime
import re

# ==========================
# 基础参数（你可以自行修改）
# ==========================
TEACHER_ID = 2                # 默认教师 ID
OUTPUT_FILE = "question_import.sql"


# ==========================
# 科目自动识别
# ==========================
def detect_subject(file_name):
    file_lower = file_name.lower()

    if "java" in file_lower:
        return "Java"
    if "database" in file_lower or "sql" in file_lower:
        return "Database"
    if "python" in file_lower:
        return "Python"
    if "c++" in file_lower or "cpp" in file_lower or "c语言" in file_lower:
        return "C++"

    return "Unknown"


# ==========================
# 难度自动识别
# (1) 文件名 (2) 题目长度
# ==========================
def detect_difficulty(file_name, content):
    file_lower = file_name.lower()

    # ---------- 文件名判定 ----------
    if "easy" in file_lower or "简单" in file_lower:
        return "EASY"
    if "medium" in file_lower or "中等" in file_lower:
        return "MEDIUM"
    if "hard" in file_lower or "困难" in file_lower:
        return "HARD"

    # ---------- 内容长度判定 ----------
    length = len(str(content))
    if length < 20:
        return "EASY"
    elif length < 50:
        return "MEDIUM"
    else:
        return "HARD"


# ==========================
# 题型识别
# ==========================
def detect_type(file_name):
    if "单选" in file_name or "single" in file_name.lower():
        return "SINGLE"
    if "多选" in file_name or "multi" in file_name.lower():
        return "MULTI"
    if "判断" in file_name or "judge" in file_name.lower():
        return "JUDGE"
    if "填空" in file_name or "blank" in file_name.lower():
        return "BLANK"
    if "问答" in file_name or "essay" in file_name.lower():
        return "ESSAY"

    return None


# ==========================
# 主程序：Excel → SQL
# ==========================
def excel_to_question_sql():
    sql_list = []
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    id_counter = 1

    excel_files = glob.glob("*.xlsx") + glob.glob("*.xls")
    print(f"发现 Excel 文件：{excel_files}")

    for file in excel_files:
        print(f"\n==== 正在处理文件：{file} ====")

        df = pd.read_excel(file)
        q_type = detect_type(file)

        if q_type is None:
            print(f"⚠ 无法识别题型：{file}")
            continue

        subject = detect_subject(file)

        for idx, row in df.iterrows():

            content = str(row.get("题目", "")).replace("'", "\"")

            # ===== options（仅选择题）=====
            options_collected = []
            if q_type in ["SINGLE", "MULTI"]:
                for opt_letter in ["A", "B", "C", "D", "E", "F"]:
                    opt_value = row.get(f"{opt_letter}选项", None)
                    if pd.notna(opt_value):
                        options_collected.append(f"{opt_letter}. {opt_value}")

                options_str = json.dumps(options_collected, ensure_ascii=False)
            else:
                options_str = "null"

            # ===== 答案 =====
            answer = str(row.get("答案", "")).replace("'", "\"")

            # ===== 自动难度识别 =====
            difficulty = detect_difficulty(file, content)

            # ===== SQL 生成 =====
            sql = f"""
INSERT INTO question (
    id, type, subject, content, options, answer, difficulty,
    teacher_id, create_time, update_time, is_deleted
) VALUES (
    {id_counter},
    '{q_type}',
    '{subject}',
    '{content}',
    {f"'{options_str}'" if options_str != "null" else "NULL"},
    '{answer}',
    '{difficulty}',
    {TEACHER_ID},
    '{now}',
    '{now}',
    0
);
"""
            sql_list.append(sql)
            id_counter += 1

    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        f.writelines(sql_list)

    print(f"\n🎉 已成功生成：{OUTPUT_FILE}")
    print("可以直接导入 MySQL。")


if __name__ == "__main__":
    excel_to_question_sql()

from fastapi import APIRouter
from pyspark.sql.functions import count, desc, col
from typing import Optional
from data.label_data import csv_top, csv_bottom, csv_outer, csv_dress
from data.spark_name import spark
from pyspark.sql import functions as F
from data import spark_name


router = APIRouter()


# 의상 대분류 4가지 상의, 하의, 아우터, 원피스 

@router.get("/search_best_combination")
async def get_items_user(
    main_category: str,
    detail_category: Optional[str] = None,
    category_color: Optional[str] = None,
    category_sub_color: Optional[str] = None,
    category_material: Optional[str] = None,
    category_detail: Optional[str] = None,
    category_print: Optional[str] = None,
    category_length: Optional[str] = None,
    category_neck_line: Optional[str] = None,
    category_collar: Optional[str] = None,
    category_fit: Optional[str] = None
):
    df = spark_name.df

    if main_category == "상의":
        result_pd_df = top_recommend_item(df, detail_category, category_color, category_sub_color, category_material, category_detail, category_print, category_length, category_neck_line, category_collar, category_fit)

    if main_category == "하의":
        result_pd_df = bottom_recommend_item(detail_category, category_color, category_sub_color, category_material, category_detail, category_print, category_length, category_fit)
    
    if main_category == "아우터":
        result_pd_df = outer_recommend_item(detail_category, category_color, category_sub_color, category_material, category_detail, category_print, category_length, category_neck_line, category_collar, category_fit)
    
    if main_category == "원피스":
        result_pd_df = dress_recommend_item(detail_category, category_color, category_sub_color, category_material, category_detail, category_print, category_length, category_neck_line, category_collar, category_fit)

    return result_pd_df.to_dict(orient="records")


def apply_optional_filters(df, conditions):
    for condition, value in conditions:
        if value is not None:
            if condition[-1] == '_':
                sub_conditions = []
                for i in range(10):
                    name = f"{condition}{i}"
                    if name in df.columns: 
                        sub_conditions.append(F.col(name) == value)
            else:
                df = df.filter(condition == value)
    return df


def top_recommend_item(
        df,
        detail_category: Optional[str] = None,
        category_color: Optional[str] = None,
        category_sub_color: Optional[str] = None,
        category_material: Optional[str] = None,
        category_detail: Optional[str] = None,
        category_print: Optional[str] = None,
        category_length: Optional[str] = None,
        category_neck_line: Optional[str] = None,
        category_collar: Optional[str] = None,
        category_fit: Optional[str] = None
    ):
    
    conditions = [
        (col(csv_top[0]), detail_category),
        (col(csv_top[1]), category_color),
        (col(csv_top[2]), category_sub_color),
        (col(csv_top[3]), category_length),
        (col(csv_top[4]), category_neck_line),
        (col(csv_top[5]), category_collar),
        (col(csv_top[6]), category_fit),
        (col(csv_top[7]), category_material),
        (col(csv_top[8]), category_detail),
        (col(csv_top[9]), category_print),
    ]

    df_filtered = apply_optional_filters(df, conditions)


    group_by_columns = [
        csv_bottom[0],
        csv_bottom[1],
        csv_bottom[2],
        csv_bottom[3],
        csv_bottom[4],
        csv_outer[0],
        csv_outer[1],
        csv_outer[2],
        csv_outer[3],
        csv_outer[4],
        csv_outer[5],
        csv_outer[6],
    ]
    
    for index in [5, 6, 7]:
        for num in range(11):
            column_name = f"{csv_bottom[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    for index in [7, 8, 9]:
        for num in range(11):
            column_name = f"{csv_outer[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df_filtered.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    # 필터 된 아이템 중심으로 추천 아이템을 필터링
    frequent_combinations = df_filtered.groupBy(*group_by_columns).agg(count("*").alias("frequency"))

    top_combination = frequent_combinations.orderBy(desc("frequency")).limit(20)
    return top_combination


def bottom_recommend_item(
        df,
        detail_category: Optional[str] = None,
        category_color: Optional[str] = None,
        category_sub_color: Optional[str] = None,
        category_material: Optional[str] = None,
        category_detail: Optional[str] = None,
        category_print: Optional[str] = None,
        category_length: Optional[str] = None,
        category_fit: Optional[str] = None
):
    conditions = [
        (col(csv_bottom[0]), detail_category),
        (col(csv_bottom[1]), category_color),
        (col(csv_bottom[2]), category_sub_color),
        (col(csv_bottom[3]), category_length),
        (col(csv_bottom[4]), category_fit),
        (col(csv_bottom[5]), category_material),
        (col(csv_bottom[6]), category_detail),
        (col(csv_bottom[7]), category_print),
    ]

    df_filtered = apply_optional_filters(df, conditions)
    
    group_by_columns = [
        csv_top[0],
        csv_top[1],
        csv_top[2],
        csv_top[3],
        csv_top[4],
        csv_top[5],
        csv_top[6],
        csv_outer[0],
        csv_outer[1],
        csv_outer[2],
        csv_outer[3],
        csv_outer[4],
        csv_outer[5],
        csv_outer[6],
    ]
    
    for index in [7, 8, 9]:
        for num in range(11):
            column_name = f"{csv_top[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    for index in [7, 8, 9]:
        for num in range(11):
            column_name = f"{csv_outer[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df_filtered.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    # 필터 된 아이템 중심으로 추천 아이템을 필터링
    frequent_combinations = df_filtered.groupBy(*group_by_columns).agg(count("*").alias("frequency"))

    bottom_combination = frequent_combinations.orderBy(desc("frequency")).limit(20)
    return bottom_combination


def outer_recommend_item(
        df,
        detail_category: Optional[str] = None,
        category_color: Optional[str] = None,
        category_sub_color: Optional[str] = None,
        category_material: Optional[str] = None,
        category_detail: Optional[str] = None,
        category_print: Optional[str] = None,
        category_length: Optional[str] = None,
        category_neck_line: Optional[str] = None,
        category_collar: Optional[str] = None,
        category_fit: Optional[str] = None
):
    
    conditions = [
        (col(csv_outer[0]), detail_category),
        (col(csv_outer[1]), category_color),
        (col(csv_outer[2]), category_sub_color),
        (col(csv_outer[3]), category_length),
        (col(csv_outer[4]), category_neck_line),
        (col(csv_outer[5]), category_collar),
        (col(csv_outer[6]), category_fit),
        (col(csv_outer[7]), category_material),
        (col(csv_outer[8]), category_detail),
        (col(csv_outer[9]), category_print),
    ]

    df_filtered = apply_optional_filters(df, conditions)
    
    # 필터 된 아이템 중심으로 추천 아이템을 필터링
    
    group_by_columns = [
        csv_bottom[0],
        csv_bottom[1],
        csv_bottom[2],
        csv_bottom[3],
        csv_bottom[4],
        csv_top[0],
        csv_top[1],
        csv_top[2],
        csv_top[3],
        csv_top[4],
        csv_top[5],
        csv_top[6],
        csv_dress[0],
        csv_dress[1],
        csv_dress[2],
        csv_dress[3],
        csv_dress[4],
        csv_dress[5],
        csv_dress[6],
    ]
    
    for index in [5, 6, 7]:
        for num in range(11):
            column_name = f"{csv_bottom[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    for index in [7, 8, 9]:
        for num in range(11):
            column_name = f"{csv_top[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df_filtered.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    for index in [7, 8, 9]:
        for num in range(11):
            column_name = f"{csv_dress[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df_filtered.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    # 필터 된 아이템 중심으로 추천 아이템을 필터링
    frequent_combinations = df_filtered.groupBy(*group_by_columns).agg(count("*").alias("frequency"))

    outer_combination = frequent_combinations.orderBy(desc("frequency")).limit(20)
    return outer_combination

def dress_recommend_item(
        df,
        detail_category: Optional[str] = None,
        category_color: Optional[str] = None,
        category_sub_color: Optional[str] = None,
        category_material: Optional[str] = None,
        category_detail: Optional[str] = None,
        category_print: Optional[str] = None,
        category_length: Optional[str] = None,
        category_neck_line: Optional[str] = None,
        category_collar: Optional[str] = None,
        category_fit: Optional[str] = None
):
    conditions = [
        (col(csv_dress[0]), detail_category),
        (col(csv_dress[1]), category_color),
        (col(csv_dress[2]), category_sub_color),
        (col(csv_dress[3]), category_length),
        (col(csv_dress[4]), category_neck_line),
        (col(csv_dress[5]), category_collar),
        (col(csv_dress[6]), category_fit),
        (col(csv_dress[7]), category_material),
        (col(csv_dress[8]), category_detail),
        (col(csv_dress[9]), category_print),
    ]

    df_filtered = apply_optional_filters(df, conditions)

    group_by_columns = [
        csv_outer[0],
        csv_outer[1],
        csv_outer[2],
        csv_outer[3],
        csv_outer[4],
        csv_outer[5],
        csv_outer[6],
    ]
    
    
    for index in [7, 8, 9]:
        for num in range(11):
            column_name = f"{csv_dress[index]}{num}"  # 숫자를 붙인 컬럼 이름 생성
            if column_name in df_filtered.columns:  # 해당 컬럼이 df_filtered에 존재하는지 확인
                group_by_columns.append(column_name)  # 존재하면 groupBy 컬럼 리스트에 추가

    # 필터 된 아이템 중심으로 추천 아이템을 필터링
    frequent_combinations = df_filtered.groupBy(*group_by_columns).agg(count("*").alias("frequency"))

    dress_combination = frequent_combinations.orderBy(desc("frequency")).limit(20)
    return dress_combination

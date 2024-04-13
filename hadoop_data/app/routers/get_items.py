from fastapi import APIRouter, Query
from pyspark.sql.functions import col, rand
from typing import Optional, List
from data.spark_name import spark
from data.label_data import csv_outer, csv_bottom, csv_dress, csv_top
from data import spark_name
import pandas as pd


router = APIRouter()


def outer_query_data(
        category: Optional[List[str]] = Query(None),
        color: Optional[List[str]] = Query(None),
        style: Optional[List[str]] = Query(None),
        sub_color: Optional[str] = None,
        material: Optional[str] = None,
        detail: Optional[str] = None,
        print: Optional[str] = None,
        length: Optional[str] = None,
        neck_line: Optional[str] = None,
        collar: Optional[str] = None,
        fit: Optional[str] = None,
    ):

    """데이터 조회 함수"""
    df = spark_name.df
    select_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상"]  # 결과에 포함될 필드 목록 초기화
    
    # 소재, 디테일, 프린트 3가지는 for문으로 순회를 해야함 
    # '스타일' 필드가 null이 아닌 행만 필터링합니다.
    filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()
    filter_condition = filter_condition & df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리"].isNotNull()
    
    if category:
        category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리") == category[0]
        for cat in category[1:]:
            category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리") == cat)
        filter_condition = filter_condition & category_condition
    
    if style:
        style_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == style[0]
        for style in style[1:]:
            style_condition = style_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == style)
        filter_condition = filter_condition & style_condition
    
    if color:
        color_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상") == color[0]
        for color in color[1:]:
            color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상") == color)
        filter_condition = filter_condition & color_condition
        


    for attr, user_input in [("소재", material), ("디테일", detail), ("프린트", print)]:
        if user_input:  # 사용자 입력값이 있는 경우
            match_found = False  # 매칭되는 데이터가 있는지 추적
            for i in range(11):  # 가능한 필드 이름 순회
                field_name = f"데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_{attr}_{i}"
                if field_name in df.columns:
                    if df[field_name] == user_input:
                        # 해당 필드가 존재하며 사용자 입력값과 일치하는지 확인
                        filter_condition = filter_condition | (df[field_name] == user_input)
                        match_found = True
                        select_fields.append(field_name)
            if not match_found:
                return pd.DataFrame()  # 매칭되는 데이터가 없으면 빈 DataFrame 반환

    # if outer_color:
    #     filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상"] == outer_color) 
    #     select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상")
    if sub_color:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_서브색상"] == sub_color) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_서브색상")
    if length:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_기장"] == length) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_기장")
    if neck_line:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_넥라인"] == neck_line) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_넥라인")
    if collar:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_옷깃"] == collar) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_옷깃")
    if fit:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_핏"] == fit) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_핏")
    

    # 최종 필터링 조건을 적용하여 데이터 필터링
    filtered_df = df.filter(filter_condition).select(*select_fields).orderBy(rand()).limit(20)

    return filtered_df.toPandas()
    

@router.get("/get_outer_items/")
async def process_and_get_data(
        category: Optional[List[str]] = Query(None),
        color: Optional[List[str]] = Query(None),
        style: Optional[List[str]] = Query(None),
        sub_color: Optional[str] = None,
        material: Optional[str] = None,
        detail: Optional[str] = None,
        print: Optional[str] = None,
        length: Optional[str] = None,
        neck_line: Optional[str] = None,
        collar: Optional[str] = None,
        fit: Optional[str] = None,
    ):
    """FastAPI 엔드포인트 함수"""
    result_pd_df = outer_query_data(category, color, style, sub_color, material, detail, print, length, neck_line, collar, fit)
    return result_pd_df.to_dict(orient="records")


def top_query_data(
        top_category: Optional[List[str]] = Query(None),
        top_color: Optional[List[str]] = Query(None),
        top_style: Optional[List[str]] = Query(None),
        top_sub_color: Optional[str] = None,
        top_material: Optional[str] = None,
        top_detail: Optional[str] = None,
        top_print: Optional[str] = None,
        top_length: Optional[str] = None,
        top_neck_line: Optional[str] = None,
        top_collar: Optional[str] = None,
        top_fit: Optional[str] = None,
    ):

    """데이터 조회 함수"""
    df = spark_name.df
    # 소재, 디테일, 프린트 3가지는 for문으로 순회를 해야함 
    select_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상"]  # 결과에 포함될 필드 목록 초기화


    # '스타일' 필드가 null이 아닌 행만 필터링합니다.
    filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()
    filter_condition = filter_condition & df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리"].isNotNull()
    if top_category:
        category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리") == top_category[0]
        for cat in top_category[1:]:
            category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리") == cat)
        filter_condition = filter_condition & category_condition
    
    if top_style:
        style_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == top_style[0]
        for style in top_style[1:]:
            style_condition = style_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == style)
        filter_condition = filter_condition & style_condition
    
    if top_color:
        color_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상") == top_color[0]
        for color in top_color[1:]:
            color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상") == color)
        filter_condition = filter_condition & color_condition
        

    for attr, user_input in [("소재", top_material), ("디테일", top_detail), ("프린트", top_print)]:
        if user_input:  # 사용자 입력값이 있는 경우
            match_found = False  # 매칭되는 데이터가 있는지 추적
            for i in range(11):  # 가능한 필드 이름 순회
                field_name = f"데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_{attr}_{i}"
                if field_name in df.columns:
                    # 해당 필드가 존재하며 사용자 입력값과 일치하는지 확인
                    if df[field_name] == user_input:
                        filter_condition = filter_condition | (df[field_name] == user_input)
                        match_found = True
                        select_fields.append(field_name)
            if not match_found:
                return pd.DataFrame()  # 매칭되는 데이터가 없으면 빈 DataFrame 반환
    
    # if top_color:
    #     filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상"] == top_color) 
    #     select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상")
    if top_sub_color:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_서브색상"] == top_sub_color) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_서브색상")
    if top_length:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_기장"] == top_length) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_기장")
    if top_neck_line:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_넥라인"] == top_neck_line) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_넥라인")
    if top_collar:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_옷깃"] == top_collar) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_옷깃")
    if top_fit:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_핏"] == top_fit) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_핏")

    

    # 최종 필터링 조건을 적용하여 데이터 필터링
    filtered_df = df.filter(filter_condition).select(*select_fields).orderBy(rand()).limit(20)

    return filtered_df.toPandas()


@router.get("/get_top_items/")
async def process_and_get_data(
        category: Optional[List[str]] = Query(None),
        color: Optional[List[str]] = Query(None),
        style: Optional[List[str]] = Query(None),
        sub_color: Optional[str] = None,
        material: Optional[str] = None,
        detail: Optional[str] = None,
        print: Optional[str] = None,
        length: Optional[str] = None,
        neck_line: Optional[str] = None,
        collar: Optional[str] = None,
        fit: Optional[str] = None,
    ):
    """FastAPI 엔드포인트 함수"""
    result_pd_df = top_query_data(category, color, style, sub_color, material, detail, print, length, neck_line, collar, fit)
    return result_pd_df.to_dict(orient="records")


def bottom_query_data(
        bottom_category: Optional[List[str]] = Query(None),
        bottom_color: Optional[List[str]] = Query(None),
        bottom_style: Optional[List[str]] = Query(None),
        bottom_sub_color: Optional[str] = None,
        bottom_material: Optional[str] = None,
        bottom_detail: Optional[str] = None,
        bottom_print: Optional[str] = None,
        bottom_length: Optional[str] = None,
        bottom_fit: Optional[str] = None,
    ):

    """데이터 조회 함수"""
    df = spark_name.df
    
    select_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상"]
    
    # '스타일' 필드가 null이 아닌 행만 필터링합니다.
    filter_condition =  df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()
    filter_condition = filter_condition & df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리"].isNotNull()

    if bottom_category:
        category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리") == bottom_category[0]
        for cat in bottom_category[1:]:
            category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리") == cat)
        filter_condition = filter_condition & category_condition
    
    if bottom_style:
        style_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == bottom_style[0]
        for style in bottom_style[1:]:
            style_condition = style_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == style)
        filter_condition = filter_condition & style_condition
    
    if bottom_color:
        color_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상") == bottom_color[0]
        for color in bottom_color[1:]:
            color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상") == color)
        filter_condition = filter_condition & color_condition


    for attr, user_input in [("소재", bottom_material), ("디테일", bottom_detail), ("프린트", bottom_print)]:
        if user_input:  # 사용자 입력값이 있는 경우
            match_found = False  # 매칭되는 데이터가 있는지 추적
            for i in range(11):  # 가능한 필드 이름 순회
                field_name = f"데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_{attr}_{i}"
                if field_name in df.columns:
                    # 해당 필드가 존재하며 사용자 입력값과 일치하는지 확인
                    if df[field_name] == user_input:
                        filter_condition = filter_condition | (df[field_name] == user_input)
                        match_found = True
                        select_fields.append(field_name)
            if not match_found:
                return pd.DataFrame()  # 매칭되는 데이터가 없으면 빈 DataFrame 반환
            
    # if bottom_color:
    #     filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상"] == bottom_color) 
    #     select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상")
    if bottom_sub_color:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_서브색상"] == bottom_sub_color) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_서브색상")
    if bottom_length:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_기장"] == bottom_length) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_기장")
    if bottom_fit:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_핏"] == bottom_fit) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_핏")


    # 최종 필터링 조건을 적용하여 데이터 필터링
    filtered_df = df.filter(filter_condition).select(*select_fields).orderBy(rand()).limit(20)

    return filtered_df.toPandas()


@router.get("/get_bottom_items/")
async def process_and_get_data(
        category: Optional[List[str]] = Query(None),
        color: Optional[List[str]] = Query(None),
        style: Optional[List[str]] = Query(None),
        sub_color: Optional[str] = None,
        material: Optional[str] = None,
        detail: Optional[str] = None,
        print: Optional[str] = None,
        length: Optional[str] = None,
        fit: Optional[str] = None,
):
    """FastAPI 엔드포인트 함수"""
    result_pd_df = bottom_query_data(category, color, style, sub_color, material, detail, print, length, fit)
    return result_pd_df.to_dict(orient="records")


def dress_query_data(
        dress_category: Optional[List[str]] = Query(None),
        dress_color: Optional[List[str]] = Query(None),
        dress_style: Optional[List[str]] = Query(None),
        dress_sub_color: Optional[str] = None,
        dress_material: Optional[str] = None,
        dress_detail: Optional[str] = None,
        dress_print: Optional[str] = None,
        dress_length: Optional[str] = None,
        dress_neck_line: Optional[str] = None,
        dress_collar: Optional[str] = None,
        dress_fit: Optional[str] = None,
    ):

    """데이터 조회 함수"""
    df = spark_name.df
    # '스타일' 필드가 null이 아닌 행만 필터링합니다.
    select_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상"]
    
    filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()
    filter_condition = filter_condition & df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리"].isNotNull()
    if dress_category:
        category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리") == dress_category[0]
        for cat in dress_category[1:]:
            category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리") == cat)
        filter_condition = filter_condition & category_condition
    
    if dress_style:
        style_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == dress_style[0]
        for style in dress_style[1:]:
            style_condition = style_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == style)
        filter_condition = filter_condition & style_condition
    
    if dress_color:
        color_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상") == dress_color[0]
        for color in dress_color[1:]:
            color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상") == color)
        filter_condition = filter_condition & color_condition
        
    

    for attr, user_input in [("소재", dress_material), ("디테일", dress_detail), ("프린트", dress_print)]:
        if user_input:  # 사용자 입력값이 있는 경우
            match_found = False  # 매칭되는 데이터가 있는지 추적
            for i in range(11):  # 가능한 필드 이름 순회
                field_name = f"데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_{attr}_{i}"
                if field_name in df.columns:
                    # 해당 필드가 존재하며 사용자 입력값과 일치하는지 확인
                    if df[field_name] == user_input:
                        filter_condition = filter_condition | (df[field_name] == user_input)
                        match_found = True
                        select_fields.append(field_name)
            if not match_found:
                return pd.DataFrame()  # 매칭되는 데이터가 없으면 빈 DataFrame 반환
    # if dress_color:
    #     filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상"] == dress_color) 
    #     select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상")
    if dress_sub_color:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_서브색상"] == dress_sub_color) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_서브색상")
    if dress_length:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_기장"] == dress_length) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_기장")
    if dress_neck_line:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_넥라인"] == dress_neck_line) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_넥라인")
    if dress_collar:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_옷깃"] == dress_collar) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_옷깃")
    if dress_fit:
        filter_condition = filter_condition & (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_핏"] == dress_fit) 
        select_fields.append("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_핏")
    

    # 최종 필터링 조건을 적용하여 데이터 필터링
    filtered_df = df.filter(filter_condition).select(*select_fields).orderBy(rand()).limit(20)

    return filtered_df.toPandas()


@router.get("/get_dress_items/")
async def process_and_get_data(
        category: Optional[List[str]] = Query(None),
        color: Optional[List[str]] = Query(None),
        style: Optional[List[str]] = Query(None),
        sub_color: Optional[str] = None,
        material: Optional[str] = None,
        detail: Optional[str] = None,
        print: Optional[str] = None,
        length: Optional[str] = None,
        neck_line: Optional[str] = None,
        collar: Optional[str] = None,
        fit: Optional[str] = None,
):
    """FastAPI 엔드포인트 함수"""
    result_pd_df = dress_query_data(category, color, style, sub_color, material, detail, print, length, neck_line, collar, fit)
    return result_pd_df.to_dict(orient="records")

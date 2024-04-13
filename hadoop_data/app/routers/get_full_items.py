from fastapi import APIRouter, Query
from pyspark.sql.functions import col, rand
from typing import Optional, List
from data import spark_name
import pandas as pd
from data.data_category import top_category, bottom_category, outer_category, dress_category
from data.data_color import data_color
from data.data_style import data_style

router = APIRouter()


@router.get("/get_full_items/")
async def get_full_items(
    category: Optional[List[str]] = Query(None),
    color: Optional[List[str]] = Query(None),
    style: Optional[List[str]] = Query(None),
):
    top_data = []

    bottom_data = []

    outer_data = []

    dress_data = []

    # csv 파일 읽어들이기
    df = spark_name.df

    top_selected_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리"]
    bottom_selected_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리"]
    outer_selected_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리"]
    dress_selected_fields = ["데이터셋 정보_파일 번호", "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리"]

    top_filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()
    bottom_filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()
    outer_filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()
    dress_filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()

    
    if category:
        for cat in category:
            if cat in top_category:
                top_data.append(cat)
            if cat in bottom_category:
                bottom_data.append(cat)
            if cat in outer_category:
                outer_data.append(cat)
            if cat in dress_category:
                dress_data.append(cat)
        if top_data:
            category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리") == top_data[0]
            for cat in top_data[1:]:
                category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리") == cat)
            top_filter_condition = top_filter_condition & category_condition
        if bottom_data:
            category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리") == bottom_data[0]
            for cat in top_data[1:]:
                category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리") == cat)
            bottom_filter_condition = bottom_filter_condition & category_condition
        if outer_data:
            category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리") == outer_data[0]
            for cat in top_data[1:]:
                category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리") == cat)
            outer_filter_condition = outer_filter_condition & category_condition
        if dress_data:
            category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리") == dress_data[0]
            for cat in top_data[1:]:
                category_condition = category_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리") == cat)
            dress_filter_condition = dress_filter_condition & category_condition
    
    colors = []

    if color:
        for c in data_color:
            colors.append(c)
        if top_data:
            color_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_컬러") == colors[0]
            for co in colors[1:]:
                color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_컬러") == co)
            top_filter_condition = top_filter_condition & color_condition
        if bottom_data:
            color_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_컬러") == colors[0]
            for co in colors[1:]:
                color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_컬러") == co)
            bottom_filter_condition = bottom_filter_condition & color_condition
        if outer_data:
            color_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_컬러") == colors[0]
            for cat in top_data[1:]:
                color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_컬러") == co)
            outer_filter_condition = outer_filter_condition & color_condition
        if dress_data:
            category_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_컬러") == colors[0]
            for cat in top_data[1:]:
                color_condition = color_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_컬러") == co)
            dress_filter_condition = dress_filter_condition & color_condition

    if style:
        style_condition = col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == style[0]
        for st in style[1:]:
            style_condition = style_condition | (col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일") == st)
        top_filter_condition = top_filter_condition & style_condition
        bottom_filter_condition = bottom_filter_condition & style_condition
        outer_filter_condition = outer_filter_condition & style_condition
        dress_filter_condition = dress_filter_condition & style_condition
    
    top_filter_df = df.filter(top_filter_condition).select(top_selected_fields).orderBy(rand()).limit(20).toPandas().to_dict(orient="records")
    bottom_filter_df = df.filter(bottom_filter_condition).select(bottom_selected_fields).orderBy(rand()).limit(20).toPandas().to_dict(orient="records")
    outer_filter_df = df.filter(outer_filter_condition).select(outer_selected_fields).orderBy(rand()).limit(20).toPandas().to_dict(orient="records")
    dress_filter_df = df.filter(dress_filter_condition).select(dress_selected_fields).orderBy(rand()).limit(20).toPandas().to_dict(orient="records")

    return {
        "top_data": top_filter_df,
        "bottom_data": bottom_filter_df,
        "outer_data": outer_filter_df,
        "dress_data": dress_filter_df,
    }
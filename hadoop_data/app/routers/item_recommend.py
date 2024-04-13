from fastapi import APIRouter
from typing import Optional
from data.spark_name import spark
from pyspark.sql.functions import rand
from data import spark_name

router = APIRouter()


def item_query_data(df, item, category):
    
    filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()

    selected_columns = ["데이터셋 정보_파일 번호", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리"]

    if item == "상의":
        filter_condition &= df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리"].isNotNull()
        selected_columns += [
            "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리",
            "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상",
            ]
        if category:
            filter_condition = filter_condition & (
                (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리"] == category)
            )
        
    if item == "하의":
        filter_condition &= df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리"].isNotNull()
        selected_columns += [
            "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리",
            "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상",
        ]
        if category:
            filter_condition = filter_condition & (
                (df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리"] == category)
            )
        
    if item == "아우터":
        filter_condition &= df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리"].isNotNull()
        selected_columns += [
            "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리",
            "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상",
        ]
        if category:
            filter_condition = filter_condition & (
                (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리"] == category)
            )
        
    if item == "원피스":
        filter_condition &= df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리"].isNotNull()
        selected_columns += [
            "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리",
            "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상",
        ]
        if category:
            filter_condition = filter_condition & (
                (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리"] == category)
            )
        
    
    filtered_df = df.filter(filter_condition).select(*selected_columns).orderBy(rand()).limit(20)
    return filtered_df
    

@router.get("/get_category_items/")
async def get_category_items(
    item: str,
    category: Optional[str] = None
):
    df = spark_name.df
    result_pd_df = item_query_data(df, item, category)
    result_pd_df = result_pd_df.toPandas()
    return result_pd_df.to_dict(orient="records")
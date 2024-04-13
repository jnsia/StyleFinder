from fastapi import APIRouter
from typing import Optional
from data.spark_name import spark
from pyspark.sql.functions import rand
from data import spark_name

router = APIRouter()


def color_query_data(df, color, sub_color):
    filter_condition = df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"].isNotNull()

    selected_columns = ["데이터셋 정보_파일 번호",
                        "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상"]

    filter_condition = filter_condition & (
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상"] == color) |
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상"] == color) |
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상"] == color) |
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상"] == color)
    )

    if sub_color:
        filter_condition = filter_condition & (
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_서브색상"] == color) |
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_서브색상"] == color) |
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_서브색상"] == color) |
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_서브색상"] == color)
    )  
        selected_columns.append("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_서브색상")
        selected_columns.append("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_서브색상")
        selected_columns.append("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_서브색상")
        selected_columns.append("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_서브색상")
    
    filtered_condition = df.filter(filter_condition).select(*selected_columns).orderBy(rand())
    return filtered_condition


@router.get("/get_color_items/")
async def get_color_items(
    color: str,
    sub_color: Optional[str] = None,
):  
    df = spark_name.df
    result_spark_df = color_query_data(df, color, sub_color)
    # result_pd_df = result_spark_df.toPandas()
    top_20_df = result_spark_df.limit(20)
    result_pd_df = top_20_df.toPandas()

    return result_pd_df.to_dict(orient="records")


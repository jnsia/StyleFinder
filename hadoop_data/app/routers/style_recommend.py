from fastapi import APIRouter
from typing import Optional
from data.spark_name import spark
from pyspark.sql.functions import rand
from data import spark_name

router = APIRouter()


def style_query_data(df, style, sub_style):
    
    selected_columns = ["데이터셋 정보_파일 번호", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일",
                        "데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리", 
                        "데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리"]

    filter_condition = (
        (df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일"] == style)
    )

    if sub_style:
        filter_condition = filter_condition & (
            (df["데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_서브스타일"] == sub_style)
        )
        selected_columns.append("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_서브스타일")

    filter_condition = df.filter(filter_condition).select(*selected_columns).orderBy(rand()).limit(20)
    return filter_condition

@router.get("/get_style_recommend/")
async def get_style_item(
    style: str,
    sub_style: Optional[str] = None
):
    df = spark_name.df
    result_pd_df = style_query_data(df, style, sub_style)
    result_pd_df = result_pd_df.toPandas()
    return result_pd_df.to_dict(orient="records")

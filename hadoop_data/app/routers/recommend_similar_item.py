from fastapi import APIRouter
from typing import Optional
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, concat_ws
from pyspark.ml.feature import Tokenizer, HashingTF, IDF, BucketedRandomProjectionLSH
from data.spark_name import spark
from data import spark_name

router = APIRouter()


@router.get("/get_similar_item")
async def input_item(
    item_number: str,
    # category: Optional[str] = None
):
    # 데이터 로드
    data = spark_name.df

    data = data.withColumn("features", concat_ws(" ",
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_카테고리"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_상의_0_색상"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_카테고리"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_하의_0_색상"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_카테고리"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_아우터_0_색상"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_카테고리"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_원피스_0_색상"),
                                             col("데이터셋 정보_데이터셋 상세설명_라벨링_스타일_0_스타일")
                                          ))
    

    # TF-IDF 계산
    tokenizer = Tokenizer(inputCol="features", outputCol="words")
    words_data = tokenizer.transform(data)

    hashing_tf = HashingTF(inputCol="words", outputCol="rawFeatures", numFeatures=20)
    featurized_data = hashing_tf.transform(words_data)

    idf = IDF(inputCol="rawFeatures", outputCol="tfidf_features")
    idf_model = idf.fit(featurized_data)
    tfidf_data = idf_model.transform(featurized_data)

    # LSH 모델을 사용하여 유사한 아이템을 빠르게 찾기
    brp = BucketedRandomProjectionLSH(inputCol="tfidf_features", outputCol="hashes", bucketLength=2.0, numHashTables=3)
    model = brp.fit(tfidf_data)

    reference = tfidf_data.filter(f"`데이터셋 정보_파일 번호` = '{item_number}'")
    result = model.approxSimilarityJoin(reference, tfidf_data, float("inf"), distCol="EuclideanDistance")

    result_df = result.select(
        "datasetA.데이터셋 정보_파일 번호",
        "datasetB.데이터셋 정보_파일 번호",
        "EuclideanDistance"
    ).orderBy("EuclideanDistance", ascending=True).limit(20).toPandas()


    return result_df.to_dict(orient="records")


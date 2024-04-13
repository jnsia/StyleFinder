from fastapi import FastAPI
from routers import color_recommend, item_recommend, style_recommend, test, get_items, recommend_similar_item, get_full_items
from data.spark_name import spark, df

app = FastAPI()

app.include_router(color_recommend.router, tags=["color"])
app.include_router(item_recommend.router,  tags=["item"])
app.include_router(style_recommend.router, tags=["style"])
app.include_router(test.router, tags=["test"])
app.include_router(get_items.router, tags=["get_items"])
app.include_router(recommend_similar_item.router, tags=["recommend"])
app.include_router(get_full_items.router, tags=["get_items"])


@app.on_event("shutdown")
def shutdown_event():
    """애플리케이션 종료 시 SparkSession 종료"""
    spark.stop()

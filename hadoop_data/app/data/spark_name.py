from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("FastAPISparkIntegration") \
    .master("yarn") \
    .getOrCreate()

df = spark.read.csv("hdfs://localhost:9000/user/ubuntu/fashion/output3.csv", header=True, inferSchema=True)
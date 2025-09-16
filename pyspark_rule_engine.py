from pyspark.sql import SparkSession
from pyspark.sql.functions import udf, col, when
from pyspark.sql.types import StringType
import pandas as pd

class PySparkRuleEngine:
    def __init__(self, app_name="GenericRuleEngine"):
        self.spark = SparkSession.builder.appName(app_name).getOrCreate()
    
    def apply_ccf_rules(self, df):
        """Apply CCF rules using Spark SQL expressions for performance"""
        return df.withColumn("result", 
            when(col("TOT_UDRWN_AM") <= 0, "DRWN")
            .when((col("TOT_UDRWN_AM") > 0) & (col("RECOURSE_MTG_CD") == "Y"), "RCRS - 100%")
            .when((col("TOT_UDRWN_AM") > 0) & (col("RECOURSE_MTG_CD") != "Y") & 
                  (col("RETAIL_SUB_PRODUCT") == "CIB Delegated Underwriting And Services"), "DUS - 100%")
            .when((col("TOT_UDRWN_AM") > 0) & (col("RECOURSE_MTG_CD") != "Y") & 
                  (col("RETAIL_SUB_PRODUCT") != "CIB Delegated Underwriting And Services") & 
                  (col("NOND_CNCL_IN") == "Y"), "UCC - 0%")
            .when((col("TOT_UDRWN_AM") > 0) & (col("RECOURSE_MTG_CD") != "Y") & 
                  (col("RETAIL_SUB_PRODUCT") != "CIB Delegated Underwriting And Services") & 
                  (col("NOND_CNCL_IN") != "Y") & (col("TOT_ORGN_TERM_CN") <= 12), "1Y - 20%")
            .otherwise("1+Y - 50%")
        )
    
    def process_csv(self, input_path, output_path=None, rules_func=None):
        """Generic method to process any CSV with custom rules"""
        df = self.spark.read.csv(input_path, header=True, inferSchema=True)
        
        if rules_func:
            result_df = rules_func(df)
        else:
            result_df = self.apply_ccf_rules(df)
        
        if output_path:
            result_df.write.mode("overwrite").csv(output_path, header=True)
        
        return result_df
    
    def stop(self):
        self.spark.stop()

def create_custom_rules(rule_config):
    """Factory function to create custom rule functions from configuration"""
    def custom_rules(df):
        result_col = df.select("*")
        for rule in rule_config:
            condition = rule["condition"]
            result_value = rule["result"]
            result_col = result_col.withColumn("result", 
                when(eval(condition), result_value).otherwise(col("result")))
        return result_col
    return custom_rules

if __name__ == "__main__":
    engine = PySparkRuleEngine()
    
    # Process with default CCF rules
    result_df = engine.process_csv("data.csv")
    result_df.select("RETAIL_SUB_PRODUCT", "TOT_UDRWN_AM", "result").show()
    
    engine.stop()
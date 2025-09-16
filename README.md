# Generic Rule Engine

A domain-agnostic, distributed rule engine supporting both Drools and PySpark for processing large datasets with configurable business rules.

## Overview

This generic rule engine can process any tabular data with configurable rules, supporting both single-machine (Drools) and distributed processing (PySpark) for large datasets.

## Project Structure

```
rule-engine-demo/
├── pom.xml                          # Maven configuration
├── requirements.txt                 # Python dependencies
├── src/main/java/com/example/
│   ├── GenericDataRecord.java       # Generic data model
│   ├── GenericRuleEngine.java       # Drools-based engine
│   ├── DataRecord.java              # Legacy specific model
│   └── RuleEngine.java              # Legacy specific engine
├── src/main/resources/
│   ├── META-INF/kmodule.xml         # Drools configuration
│   └── ccf-rules.drl                # Generic rules file
├── pyspark_rule_engine.py           # PySpark distributed engine
└── rule_config_generator.py         # Rule configuration generator
```

## Usage

### Java/Drools Engine (Single Machine)
```bash
# Generic engine
mvn compile exec:java -Dexec.mainClass="com.example.GenericRuleEngine"

# Legacy specific engine
mvn compile exec:java -Dexec.mainClass="com.example.RuleEngine"
```

### PySpark Engine (Distributed)
```python
from pyspark_rule_engine import PySparkRuleEngine

engine = PySparkRuleEngine()
result_df = engine.process_csv("data.csv")
result_df.show()
engine.stop()
```

### Rule Configuration Generation
```python
from rule_config_generator import RuleConfigGenerator

# Generate Drools rules from CSV
RuleConfigGenerator.csv_to_drools("rules_clean.csv", "generated-rules.drl")

# Generate PySpark config
rules = RuleConfigGenerator.csv_to_pyspark_config("rules_clean.csv")
```

## Key Features

### Generic & Domain-Agnostic
- **Field-based Processing**: Uses `Map<String, Object>` for any data structure
- **Configurable Rules**: Rules defined by field names, not hardcoded properties
- **CSV-driven**: Automatic field detection from CSV headers

### Performance & Scalability
- **Parallel Processing**: Java streams with `parallelStream()`
- **Distributed Computing**: PySpark support for large datasets
- **Memory Efficient**: Streaming CSV processing
- **Session Management**: Proper KieSession lifecycle

### Rule Management
- **Priority-based**: Salience-based rule ordering
- **Dynamic Generation**: Convert CSV rule definitions to DRL/PySpark
- **Multiple Formats**: Support for Drools DRL and PySpark conditions

## Architecture

### Single Machine (Drools)
- Uses `GenericDataRecord` with field-based access
- Parallel processing with Java streams
- Memory-efficient CSV streaming

### Distributed (PySpark)
- Spark SQL expressions for optimal performance
- Automatic partitioning and distribution
- Support for large datasets (TB+)

## Dependencies

### Java
- **Drools 7.74.1.Final**: Rule engine
- **OpenCSV 5.7.1**: CSV processing
- **Java 11+**: Runtime

### Python
- **PySpark 3.4+**: Distributed processing
- **Pandas 1.5+**: Data manipulation
import pandas as pd
import json

class RuleConfigGenerator:
    """Generate rule configurations from CSV rule definitions"""
    
    @staticmethod
    def csv_to_drools(rules_csv_path, output_drl_path):
        """Convert CSV rules to Drools DRL format"""
        df = pd.read_csv(rules_csv_path)
        
        drl_content = "package rules\n\nimport com.example.GenericDataRecord\n\n"
        
        for _, rule in df.iterrows():
            rule_name = rule['Rule_Name'].replace(' ', '_')
            sequence = rule['Sequence']
            condition = rule['Rule_Logic']
            pass_result = rule['Pass_Action_Value']
            fail_result = rule.get('Fail_Action_Value', 'DEFAULT')
            
            # Convert condition to Drools format
            drools_condition = RuleConfigGenerator._convert_condition(condition)
            
            drl_content += f'''rule "{rule_name}"
    salience {110 - sequence}
    when
        $record : GenericDataRecord({drools_condition})
    then
        $record.setResult("{pass_result}");
end

'''
        
        with open(output_drl_path, 'w') as f:
            f.write(drl_content)
    
    @staticmethod
    def csv_to_pyspark_config(rules_csv_path):
        """Convert CSV rules to PySpark configuration"""
        df = pd.read_csv(rules_csv_path)
        rules = []
        
        for _, rule in df.iterrows():
            condition = RuleConfigGenerator._convert_to_pyspark_condition(rule['Rule_Logic'])
            rules.append({
                "name": rule['Rule_Name'],
                "sequence": rule['Sequence'],
                "condition": condition,
                "result": rule['Pass_Action_Value']
            })
        
        return sorted(rules, key=lambda x: x['sequence'])
    
    @staticmethod
    def _convert_condition(condition_str):
        """Convert business logic to Drools condition format"""
        # Simple conversion - can be enhanced for complex conditions
        condition_str = condition_str.replace('=', '==')
        condition_str = condition_str.replace('>', '>')
        condition_str = condition_str.replace('<', '<')
        
        # Convert field references to getField calls
        import re
        field_pattern = r'\b([A-Z_]+)\b'
        
        def replace_field(match):
            field_name = match.group(1)
            if field_name in ['AND', 'OR', 'NOT']:
                return field_name.lower()
            return f'getField("{field_name}") != null && getField("{field_name}")'
        
        return re.sub(field_pattern, replace_field, condition_str)
    
    @staticmethod
    def _convert_to_pyspark_condition(condition_str):
        """Convert business logic to PySpark condition format"""
        # Convert to PySpark column expressions
        condition_str = condition_str.replace('=', '==')
        
        import re
        field_pattern = r'\b([A-Z_]+)\b'
        
        def replace_field(match):
            field_name = match.group(1)
            if field_name in ['AND', 'OR', 'NOT']:
                return f' {field_name.lower()} '
            return f'col("{field_name}")'
        
        return re.sub(field_pattern, replace_field, condition_str)

if __name__ == "__main__":
    generator = RuleConfigGenerator()
    
    # Generate Drools rules from CSV
    generator.csv_to_drools("rules_clean.csv", "src/main/resources/generated-rules.drl")
    
    # Generate PySpark config
    pyspark_rules = generator.csv_to_pyspark_config("rules_clean.csv")
    print(json.dumps(pyspark_rules, indent=2))
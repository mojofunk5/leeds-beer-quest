import argparse

def fix_value_quotes(individual_value):
	if individual_value.startswith('"'):
		individual_value = individual_value[1:]
	if individual_value.endswith('"'):
		individual_value = individual_value[0:-1]

	if "+00:00" in individual_value:
		individual_value = individual_value.replace("+00:00", "").replace('T', ' ')
		
	individual_value = individual_value.replace("'", "''")

	try:
		float(individual_value)
	except ValueError:
		individual_value = "'" + individual_value + "'"
	
	return individual_value

def fix_record_quotes(record):
	individual_values = record.split('",')
	return ','.join([fix_value_quotes(individual_value) for individual_value in individual_values])

parser = argparse.ArgumentParser(description='Converts the leeds beer quest csv data set into a series of sql insert statements to load into the database')
parser.add_argument('--input', '-i', help='input file', required=True)
parser.add_argument('--output', '-o', help='output file', required=True)
args = parser.parse_args()

print(f'Running with input=[{args.input}] and output=[{args.output}]')

entries = [f.strip() for f in open(args.input, 'r')]

columns=entries[0].replace('"', '')
records=entries[1:]

inserts = [f'INSERT INTO VENUE ({columns}) VALUES ({fix_record_quotes(record)});' for record in records]

f = open(args.output, 'w')
for insert in inserts:
	f.write(insert + '\n')
f.close()
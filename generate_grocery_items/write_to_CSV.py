import pandas as pd

df = pd.read_json(open("./items.json", encoding="utf-8"))
df.to_csv("temp_data.csv", encoding="utf-8", index=False)
f = open("temp_data.csv", "r")
line = f.readline()
while line:
    split_line = line.split(",")
    if split_line[0].isdigit():
        print(split_line[0], split_line[2])
    line = f.readline()
f.close()
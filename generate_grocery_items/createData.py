import os

f = open("BigBasket.csv", "r")
os.system("touch storeDB.txt && echo > storeDB.txt")
f2 = open("storeDB.txt", "w")
lines = f.readlines()
for line in lines:
	l1 = line.split(",")
	for i in range(len(l1)):
		if (i == 0 or i == 1 or i == 2 or i == 7):
			f2.write(str(l1[i]).replace(" ", "") + " ")
	f2.write("\n")

f2.close()
f.close()
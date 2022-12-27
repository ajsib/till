import os
import random

f = open("BigBasket.csv", "r")
os.system("touch storeDB.txt && echo > storeDB.txt")
f2 = open("storeDB.txt", "w")
lines = f.readlines()
used_barcodes = []
count = 0
for line in lines:
	l1 = line.split(",")
	count += 1
	if len(l1) == len(lines[0].split(",")) and l1[1] != "bb Combo" and l1[5] != "Combo":
		for i in range(len(l1)):
			if (i == 0 or i == 1 or i == 5 or  i == 6 or i == 2):
				if (i == 2 and count > 1):
					exchange_rate = 0.016
					tmp = float(l1[i])
					tmp *= exchange_rate
					l1[i] = tmp.__round__(2)
				f2.write(" " + str(l1[i]).replace(" ", ""))
		if (count == 1):
			f2.write(" Barcode Quantity\n")
		else:
			barcode = random.randrange(1000000000000, 9999999999999)
			qty = random.randrange(29, 400)
			while barcode in used_barcodes:
				barcode = random.randrange(1000000000000, 9999999999999)
			used_barcodes.append(barcode)
			s = " " + str(barcode) + " " + str(qty) + "\n"
			f2.write(s)

	else: pass
f2.close()
f.close()
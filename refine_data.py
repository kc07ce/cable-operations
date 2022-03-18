import csv
import datetime
from numpy import True_
import pandas as pd


def get_month(x):
    if 'JAN' in x:
        return 1
    elif 'FEB' in x:
        return 2
    elif 'MAR' in x:
        return 3
    elif 'APR' in x:
        return 4
    elif 'MAY' in x:
        return 5
    elif 'JUN' in x:
        return 6
    elif 'JUL' in x:
        return 7
    elif 'AUG' in x:
        return 8
    elif 'SEP' in x:
        return 9
    elif 'OCT' in x:
        return 10
    elif 'NOV' in x:
        return 11
    elif 'DEC' in x:
        return 12


file_name = 'cust_box.xlsx'
f = open(file_name, 'r')
col_head = ['ID', 'NAME', 'DOOR_NO', 'PHONE_NO',
            'ACCOUNT_NO', 'BOX_NO', 'CREATED_ON', 'STB_STATUS', 'COLLECTED_BY']

df = pd.read_excel(file_name, sheet_name='RAJU CABLE ')
df = df.fillna('')
cust_id = 1
stb_id = 1
cust_stb_id = 1
customers = csv.writer(open('customers.csv', 'w+'))
customers.writerow(
    ['CID', 'NAME', 'DOOR_NO', 'PHONE_NO', 'TOTAL_BAL', 'CREATED_ON'])

stb = csv.writer(open('stb.csv', 'w+'))
stb.writerow(['STB_ID', 'ACCOUNT_NO', 'STB_NO', 'CREATED_ON'])

customers_stb = csv.writer(open('customers_stb.csv', 'w+'))
customers_stb.writerow(['ID', 'CID', 'STB_ID', 'CREATED_ON', 'STATUS'])

today = datetime.datetime.now()
for i in df.index:
    row = df.iloc[i]
    cust_exit = False
    if str(df.iloc[i]['NAME']) != '':
        cust_exit = True
        l = [cust_id, str(row['NAME']).strip(), str(row['DOOR_NO']).strip(),
             row['PHONE_NO'], int(row['TOTAL_BAL']), '']
        customers.writerow(l)
        cust_id = cust_id+1
    box_exist = False
    if str(df.iloc[i]['BOX_NO']) != '' or str(df.iloc[i]['ACCOUNT']) != '':
        box_exist = True
        account_no = int(row['ACCOUNT']) if row['ACCOUNT'] != '' else ''
        box_no = str(row['BOX_NO']).strip()
        g = [stb_id, account_no, box_no, '']
        stb.writerow(g)
        stb_id = stb_id+1
    if cust_exit and box_exist:
        customers_stb.writerow(
            [cust_stb_id, cust_id-1, stb_id-1, '', 'CURRENT'])
        cust_stb_id = cust_stb_id+1

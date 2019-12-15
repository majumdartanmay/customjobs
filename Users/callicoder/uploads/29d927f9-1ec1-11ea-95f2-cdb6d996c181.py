import pygsheets
import requests
import json
import time
import csv
from pymongo import MongoClient

#gc = pygsheets.authorize()
#sh = gc.open('CF Reassign')
#wks = sh.sheet1
#print(list(wks))
client_id = 'A91B82DE3E93446A8141A52F288F69EFA1B09B1D13BB4E55BE743AB547B3489E'
assign_url = "http://support.withfloats.com/Home/AssignAccountManagerForFloatingPoint"
un_assign_url = "http://support.withfloats.com/Home/UnassignAccountManagerForFloatingPoint"

def get_database_connection(db):
    float_server = MongoClient(
        'mongodb+srv://wf_api_service:96d^ZhhgcFQZs5^o@mumbai-mm1n2.mongodb.net/floatdb?retryWrites=true')
    float_database = float_server.floatdb

    db_switcher = \
        {
            'FloatingPointInternal': float_database.floatingPointInternal,
            'CIC': float_database.customerInvoiceCollection,
            'FPInternalPropertiesCollection': float_database.fpInternalPropertiesCollection,
            'FPPaidWidgetRequestCollection': float_database.fpPaidWidgetRequestCollection,
            'FPWidgetPacksCollection': float_database.fpWidgetPacksCollection,
            'FPVirtualNumberPool': float_database.fpVirtualNumbersPoolCollection,
            'CallTrackerMaps': float_database.callTrackerMaps,
            'PartnerUserProfile': float_database.partneruserprofiles,
            'PartnerWalletLog': float_database.PartnerWalletLogs,
            'KitsuneSchemaDB': float_server.kitsuneschemadb,
	    'fpInternalPropertiesCollection': float_database.fpInternalPropertiesCollection
        }

    return db_switcher.get(db, None)

print("=======a========")
#with open('CF Reassign - CF Reassign.csv', 'rb') as cf_assign_file:
 #   csv_reader = csv.reader(cf_assign_file)
  #  print"=====csv_reader=====",csv_reader

reader = csv.reader(open('CF Reassign - CF Reassign.csv','rb'))

i = 0
for row in reader: 
    
	i = i + 1
   # print("========i, row=======", i, row)
    if i is 1 :
        continue

	
	fptag = row[0]
	cf = row[2]
	FloatingPointInternalResult = list(get_database_connection('FloatingPointInternal').find(
				{ 
					"FloatingPointTag" : fptag

				}, 
				{ 
					"FloatingPointId" : 1.0
				}))

	fptag_id = FloatingPointInternalResult[0]['FloatingPointId']

	PartnerUserProfileResult = list(get_database_connection('PartnerUserProfile').find(
				{ 
					"username" : cf 

				}))
	cf_id = PartnerUserProfileResult[0]['_id']
	print("===cf_id====",cf_id)


	fpInternalPropertiesCollectionResult = list(get_database_connection('fpInternalPropertiesCollection').find(
				{ 
					"FpId" : fptag_id

				}))


	for vals in fpInternalPropertiesCollectionResult:
    
	    tag = 0
	    print("========vals=====a=======", vals['AccountManagers'])
	    fp_collection_id = vals['_id']
	    print("======fp_collection_id====", fp_collection_id)
	    count = len(vals['AccountManagers'])
	    print("=====count======", count)
	    for am in vals['AccountManagers']:
	        count = count - 1
		if am['ProfileAccessType'] == 3:
		    am['ProfileId'] = cf_id
		    tag = 1
		    break
		elif tag == 0 and count == 0:
		    vals['AccountManagers'].update({'ProfileId': cf_id, 'ProfileAccessType': 3, 'ProfileRoleType': 2})
	    
 	    print("=====vals['AccountManagers']==b==", vals['AccountManagers'])
	    update_query_widget = {"AccountManagers" : vals['AccountManagers']}
	    get_database_connection('fpInternalPropertiesCollection').update_one({'_id': fp_collection_id, 'FpId': fptag_id}, {'$set': update_query_widget})

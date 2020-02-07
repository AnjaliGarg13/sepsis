from flask import Flask, request, jsonify

import os
import pymongo
import dns
from flask_pymongo import PyMongo
from ml_models.ocr import *
from Sepsis_predictor.prediction_script import *

app = Flask(__name__)

app.config['REPORT_UPLOADS'] = "./report_uploads"


app.config['MONGO_URI'] = 'mongodb+srv://skepticUser:skeptic@cluster0-l4moq.mongodb.net/test?retryWrites=true&w=majority'
mongo = PyMongo(app)

# @app.route('/frame', methods=['GET'])
# def get_all_frameworks():
#     u_collection = mongo.db.sample1
#     print("hiiiiiiiiiiiiii")
#     u_collection.insert_one({'name': 'Anjali'})
#     # u_collection.insert_one({'name': 'Anjali'})
#     # u_collection.insert_one({'name': 'Anth'})
#     # u_collection.insert_one({'name': 'Antho'})
#     output = ['hi there']
#     return jsonify(output)

# @app.route('/dbtry', methods=['GET'])
# def tryyy():
#     u_collect = mongo.db.tryyyy
#     print("hiiiiiiiiiiyyyi")
#     u_collect.insert_one({'name': 'Anjaliii', 'subjects': "COE"})
#     # u_collect.insert_one({'name': 'Anjali'})
#     # u_collect.insert_one({'name': 'Anth'})
#     # u_collect.insert_one({'name': 'Antho'})
#     output = ['hi there']
#     return jsonify(output)

@app.route('/create_new_patient_entry', methods=['POST'])
def add_patient():
    if request.method == 'POST':
        patient_json = request.get_json()
        name = patient_json['patient_name']
        # id = name+"_Record"
        # print(id)
        # patient_json[''] = mongo.db.name
        # mongo.db.createCollection(name)
        mongo.db[name].insert_one(patient_json)
    return "patient " + name + " added"

@app.route('/upload_lab_report', methods=['POST'])
def upload_lab_report():
    if request.method == 'POST':
        if request.files:
            file = request.files['file']
            if file.filename == '':
                return "empty"
            if file:
                filename= file.filename
                file.save(os.path.join(app.config['REPORT_UPLOADS'],filename))
                # mongo.db.anjali.insert_one(retrieveInfoFromImage(filename))
                data_test = retrieveInfoFromImage(os.path.join(app.config['REPORT_UPLOADS'],filename))

                apap = find_sepsis_prob(retrieveInfoFromImage(os.path.join(app.config['REPORT_UPLOADS'], filename)))
                print(apap)
                data_test['risk'] = apap
                return data_test
                # print(test(retrieveInfoFromImage(filename)))
                # print(find_sepsis_prob(retrieveInfoFromImage(filename)))
                # return retrieveInfoFromImage(os.path.join(app.config['REPORT_UPLOADS'],filename))
                # print(filename)
                # result = lab_report_value_matrix("./report_uploads"+filename)
                # return str(result)
    return "waiting"

@app.route('/')
def index():
    print("hiiii")
    return "<h1> HIIII</h1>"

if  __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
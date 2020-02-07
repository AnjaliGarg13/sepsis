import pandas as pd
import numpy as np
from numpy import array
import csv
from pandas import DataFrame
import os
from scipy.stats import entropy
import scipy as sc
import pickle
import shutil, zipfile


def predict_sepsis(test_data, trained_model):
    data = test_data
    data.fillna(method='ffill', axis=0, inplace=True)
    data = pd.DataFrame(data).fillna(0)

    data['ID'] = 0

    DBP = pd.pivot_table(data, values='DBP', index='ID', columns='ICULOS')
    O2Sat = pd.pivot_table(data, values='O2Sat', index='ID', columns='ICULOS')
    Temp = pd.pivot_table(data, values='Temp', index='ID', columns='ICULOS')
    RR = pd.pivot_table(data, values='Resp', index='ID', columns='ICULOS')
    BP = pd.pivot_table(data, values='SBP', index='ID', columns='ICULOS')
    HR = pd.pivot_table(data, values='HR', index='ID', columns='ICULOS')

    Heart_rate = HR.fillna(0)
    Resp_rate = RR.fillna(0)
    Blood_press = BP.fillna(0)
    DBP_ = DBP.fillna(0)
    Temp_ = Temp.fillna(0)
    O2Sat_ = O2Sat.fillna(0)

    score_list = [0.9, 0.9, 0.9, 0.9, 0.9, 0.9]
    label_list = [1, 1, 1, 1, 1, 1]

    score_pred = []
    label_pred = []

    for iterat in range(0, Resp_rate.shape[1] - 6):

        for i in range(iterat, iterat + 1):

            Heart_rate_test = Heart_rate.iloc[: i:i + 6]
            Resp_rate_test = Resp_rate.iloc[:, i:i + 6]
            Blood_press_test = Blood_press.iloc[:, i:i + 6]
            Temp_test = Temp_.iloc[:, i:i + 6]
            DBP_test = DBP_.iloc[:, i:i + 6]
            O2Sat_test = O2Sat_.iloc[:, i:i + 6]

            Heart_rate['HR_min'] = Heart_rate_test.min(axis=1)
            Heart_rate['HR_mean'] = Heart_rate_test.mean(axis=1)
            Heart_rate['HR_max'] = Heart_rate_test.max(axis=1)
            Heart_rate['HR_stdev'] = Heart_rate_test.std(axis=1)
            Heart_rate['HR_var'] = Heart_rate_test.var(axis=1)
            Heart_rate['HR_skew'] = Heart_rate_test.skew(axis=1)
            Heart_rate['HR_kurt'] = Heart_rate_test.kurt(axis=1)

            Heart_rate['BP_min'] = Blood_press_test.min(axis=1)
            Heart_rate['BP_mean'] = Blood_press_test.mean(axis=1)
            Heart_rate['BP_max'] = Blood_press_test.max(axis=1)
            Heart_rate['BP_stdev'] = Blood_press_test.std(axis=1)
            Heart_rate['BP_var'] = Blood_press_test.var(axis=1)
            Heart_rate['BP_skew'] = Blood_press_test.skew(axis=1)
            Heart_rate['BP_kurt'] = Blood_press_test.kurt(axis=1)

            Heart_rate['RR_min'] = Resp_rate_test.min(axis=1)
            Heart_rate['RR_mean'] = Resp_rate_test.mean(axis=1)
            Heart_rate['RR_max'] = Resp_rate_test.max(axis=1)
            Heart_rate['RR_stdev'] = Resp_rate_test.std(axis=1)
            Heart_rate['RR_var'] = Resp_rate_test.var(axis=1)
            Heart_rate['RR_skew'] = Resp_rate_test.skew(axis=1)
            Heart_rate['RR_kurt'] = Resp_rate_test.kurt(axis=1)

            Heart_rate['DBP_min'] = DBP_test.min(axis=1)
            Heart_rate['DBP_mean'] = DBP_test.mean(axis=1)
            Heart_rate['DBP_max'] = DBP_test.max(axis=1)
            Heart_rate['DBP_stdev'] = DBP_test.std(axis=1)
            Heart_rate['DBP_var'] = DBP_test.var(axis=1)
            Heart_rate['DBP_skew'] = DBP_test.skew(axis=1)
            Heart_rate['DBP_kurt'] = DBP_test.kurt(axis=1)

            Heart_rate['O2Sat_min'] = O2Sat_test.min(axis=1)
            Heart_rate['O2Sat_mean'] = O2Sat_test.mean(axis=1)
            Heart_rate['O2Sat_max'] = O2Sat_test.max(axis=1)
            Heart_rate['O2Sat_stdev'] = O2Sat_test.std(axis=1)
            Heart_rate['O2Sat_var'] = O2Sat_test.var(axis=1)
            Heart_rate['O2Sat_skew'] = O2Sat_test.skew(axis=1)
            Heart_rate['O2Sat_kurt'] = O2Sat_test.kurt(axis=1)

            Heart_rate['Temp_min'] = Temp_test.min(axis=1)
            Heart_rate['Temp_mean'] = Temp_test.mean(axis=1)
            Heart_rate['Temp_max'] = Temp_test.max(axis=1)
            Heart_rate['Temp_stdev'] = Temp_test.std(axis=1)
            Heart_rate['Temp_var'] = Temp_test.var(axis=1)
            Heart_rate['Temp_skew'] = Temp_test.skew(axis=1)
            Heart_rate['Temp_kurt'] = Temp_test.kurt(axis=1)

            X_test = Heart_rate.values[:, Temp_test.shape[1]:Temp_test.shape[1] + 42]

            score = trained_model.predict_proba(X_test)

            score_pred.append(score[0][1])

            if score_pred[0] >= 0.5:
                labels = 1
            else:
                labels = 0

            label_pred.append(labels)

    return score_pred[-1]


def read_challenge_data(input_file):
    data1 = pd.DataFrame()
    with open(input_file, 'r') as f:
        header = f.readline().strip()
        column_names = header.split('|')
        data = np.loadtxt(f, delimiter='|')

    # ignore SepsisLabel column if present
    if column_names[-1] == 'SepsisLabel':
        column_names = column_names[:-1]
        data = data[:, :-1]
        data1 = pd.DataFrame(data, columns=column_names)
    return data1

def test(parameters):
    files = [f for f in os.listdir('.') if os.path.isfile(f)]
    print(files)
    return files


def find_sepsis_prob(parameters):
    test_data = read_challenge_data("Sepsis_predictor/test1.psv")

    a, b = np.shape(test_data)

    hh = {}
    hh['HR'] = parameters['Heart Rate']

    hh['O2Sat'] = parameters['O2Sat']

    hh['Temp'] = parameters['Temperature']

    hh['EtCO2'] = parameters['EtCO2']

    test_data = test_data.append(hh, ignore_index=True)

    trained_model = pickle.load(open('Sepsis_predictor/saved_model_1.pkl', 'rb'))

    return predict_sepsis(test_data, trained_model)


if __name__ == "__main__":
    hh = {}
    hh['Heart Rate'] = 32
    hh['O2Sat'] = 23
    hh['Temperature'] = 23
    hh['EtCO2'] = 23

    print(find_sepsis_prob(hh))
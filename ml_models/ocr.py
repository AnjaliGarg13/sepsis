from PIL import Image
import pytesseract
import argparse
import cv2
import os


# class GenerateInfoFromReport:
#     # def __init__(self):

def retrieveInfoFromImage(ImageAddress):
    image = cv2.imread(ImageAddress)
    if image is None:
        print("Wrong Address")
        return {}
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    pil_image = Image.fromarray(cv2.cvtColor(gray, cv2.COLOR_BGR2RGB))
    text = pytesseract.image_to_string(pil_image)
    params = {}

    search_strings = ["Heart Rate", "O2Sat", "Temperature", "SBP", "MAP", "DBP", "Respiration Rate", "EtCO2", "BaseExcess", "HCO3", "Fio2", "pH", "PaCO2", "SaO2", "AST", "BUN", "Alka;inephos", "HIV Test"]

        # Code to calculate the index from where the test results start
    for test in search_strings:
        index = text.find(test)
        if index is not -1:
            result = ""
            index = index + len(test) + 1
            while index < len(text) and text[index] is not ' ' and text[index] is not '\n' and text[index] is not '!':
                result = result + (text[index])
                index = index + 1
            if ord(result[0]) >= 48 and ord(result[0]) < 58:
                params[test] = int(result)
            else:
                params[test] = result
    return params


'''
#Code for testing on terminal
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", required=True,	help="path to input image to be OCR'd")
ap.add_argument("-p", "--preprocess", type=str, default="thresh", help="type of preprocessing to be done")
args = vars(ap.parse_args())

func = GenerateInfoFromReport()
addr = args["image"]
func.retrieveInfoFromImage(addr)


'''




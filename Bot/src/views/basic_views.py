#!/usr/bin/env python
# -*- coding: utf-8 -*-
from yowsup.layers.protocol_messages.protocolentities import TextMessageProtocolEntity
import random
import os
import unirest
import requests


def get_base_directory_path():
        return os.getcwd()


def get_base_file_path():
        return os.getcwd() + '/players.txt'


def get_base_file_size_path():
        return os.getcwd() + '/size.txt'

def get_base_file_time_path():
        return os.getcwd() + '/time.txt'


def get_base_file_duration_path():
        return os.getcwd() + '/duration.txt'


def start_partido(message, match):
    number = match.group("answer")
    hora = match.group("hora")
    duration = match.group("duration")
    if number.isdigit():
        print number
        with open(get_base_file_size_path(), 'w+') as f:
            f.write(number)
        f.close()

        with open(get_base_file_time_path(), 'w+') as f:
            f.write(hora)
        f.close()

        with open(get_base_file_duration_path(), 'w+') as f:
            f.write(duration)
        f.close()
        return TextMessageProtocolEntity("Started, #{word} to enter", to=message.getFrom())
    else:
        return TextMessageProtocolEntity("Invalid Size", to=message.getFrom())


def save_player_to_disk(person, message, match):  # pragma: no cover
    with open(get_base_file_path(), 'a+b') as f:
        f.write(person)
        f.write('\n')
    f.close()
    return TextMessageProtocolEntity("Registrado " + person, to=message.getFrom())


def check_disk(message, match):
    size_max = 10
    # string_person = message.getParticipant()
    string_person = get_nombre(message.getParticipant())
    t = open(get_base_file_size_path(), "r")
    for line in t.readlines():
        size_max = int(line)

    f = open(get_base_file_path(), "r")
    counter = 0
    for line in f.readlines():
        counter += 1
        print line
        if line[:10] == string_person[:10]:
            return TextMessageProtocolEntity("Already registered.", to=message.getFrom())

    if counter == size_max:
        return TextMessageProtocolEntity("Limit reached.", to=message.getFrom())
    save_player_to_disk(string_person, message, match)


def list_players(message, match):
    f = open(get_base_file_path(), "r")
    # Print all its lines.
    players = []
    for line in f.readlines():
        players.append(line)
    string = ', '.join(players)
    return TextMessageProtocolEntity(string, to=message.getFrom())


def partido_end(message, match):
    open(get_base_file_path(), 'w').close()
    return TextMessageProtocolEntity("List cleared.", to=message.getFrom())

# def list_all(message, match):
#     time = match.group("hora")
#     r = requests.get('http://45.55.30.36/api/cancha?lat=25.652061&long=-100.286438&year=2016&month=1&day=22&start_time=' + hora[:2] + '&end_time=' + str(int(hora[:2]) + 1) + '&dist=100')
#     result = r.json()

def close(message, match):
    print 'closing'
    z = open(get_base_file_time_path(), "r")
    for line in z.readlines():
        hora = str(line)
    t = open(get_base_file_duration_path(), "r")
    for line in t.readlines():
        duration = line
    f = open(get_base_file_path(), "r")
    # Print all its lines.
    players = []
    for line in f.readlines():
        players.append(line[:-1])
    string = ', '.join(players)
    print hora[:2]
    print str(int(hora[:2]) + 1)
    print('http://45.55.30.36/api/cancha?lat=25.652061&long=-100.286438&year=2016&month=1&day=22&start_time=' + hora[:2] + '&end_time=' + str(int(hora[:2]) + 1) + '&dist=100')
    r = requests.get('http://45.55.30.36/api/cancha?lat=25.652061&long=-100.286438&year=2016&month=1&day=22&start_time=' + hora[:2] + '&end_time=' + str(int(hora[:2]) + 1) + '&dist=100')
    json = r.json()
    # print json[0]
    i = 0
    for idx, game in enumerate(json):
        if game['name'] == 'Cancha Tec':
            i = idx

    open(get_base_file_path(), 'w').close()
    print('http://45.55.30.36:3000/api/reserveSpot/' + str(json[i]['rentas'][0]) + '/2016-02-21')
    r = requests.get('http://45.55.30.36:3000/api/reserveSpot/' + str(json[i]['rentas'][0]) + '/2016-02-21')
    json2 = r.json()
    confcode = json2["confirmation_code"]
    return TextMessageProtocolEntity("Confirmados a las " + str(hora) + " en " + json[i]['name'] + ". Codigo: " + str(confcode)[:5] + '.  ' + str(string), to=message.getFrom())


def get_nombre(id):
    if id == "5218110663456@s.whatsapp.net":
        nombres = ["Rana", "Adrian"]
        return randstr(nombres)

    elif id == "5218112125597@s.whatsapp.net":
        nombres = ["Adrian", "Lozano"]
        return randstr(nombres)

    elif id == "5218117471775@s.whatsapp.net":
        nombres = ["David", "Edrel"]
        return randstr(nombres)

    elif id == "5218116618135@s.whatsapp.net":
        nombres = ["Eduardo"]
        return randstr(nombres)

    elif id == "5218183660872@s.whatsapp.net":
        nombres = ["Esteban", "Beban"]
        return randstr(nombres)

    elif id == "17204742885@s.whatsapp.net":
        nombres = ["Fred"]
        return randstr(nombres)

    elif id == "5218110801239@s.whatsapp.net":
        nombres = ["Gabriel", "Guerra"]
        return randstr(nombres)

    elif id == "5218112807383@s.whatsapp.net":
        nombres = ["Ortiz", "Gabriel"]
        return randstr(nombres)

    elif id in ("353892498794@s.whatsapp.net", "5218117793991@s.whatsapp.net"):
        nombres = ["Jorge"]
        return randstr(nombres)

    elif id == "14389985110@s.whatsapp.net":
        nombres = ["Justin"]
        return randstr(nombres)

    elif id == "5218121217755@s.whatsapp.net":
        nombres = ["Manuel"]
        return randstr(nombres)

    elif id == "5218110778105@s.whatsapp.net":
        nombres = ["Mauricio", "Cantu"]
        return randstr(nombres)

    elif id == "5218112114862@s.whatsapp.net":
        nombres = ["Garcia Garica", "Mau"]
        return randstr(nombres)

    elif id == "447463772183@s.whatsapp.net":
        nombres = ["Marchand"]
        return randstr(nombres)

    elif id == "5218112422227@s.whatsapp.net":
        nombres = ["Europeña", "Peña"]
        return randstr(nombres)

    elif id == "5218115445683@s.whatsapp.net":
        nombres = ["Memo", "Guillermo"]
        return randstr(nombres)

    elif id == "5218117862786@s.whatsapp.net":
        nombres = ["Olaf", "Tlahui"]
        return randstr(nombres)

    elif id == "19564839570@s.whatsapp.net":
        nombres = ["Oscar"]
        return randstr(nombres)

    elif id == "5218115995291@s.whatsapp.net":
        nombres = ["Patfuck", "Pato"]
        return randstr(nombres)

    elif id == "5218115448028@s.whatsapp.net":
        nombres = ["Vela"]
        return randstr(nombres)

    elif id == "5218182560710@s.whatsapp.net":
        nombres = ["Ricky", "Ricardo"]
        return randstr(nombres)

    elif id == "14255057149@s.whatsapp.net":
        nombres = ["Victor"]
        return randstr(nombres)

    elif id == "16785766095@s.whatsapp.net":
        nombres = ["Fernando", "Echeverry"]
        return randstr(nombres)

    elif id == "5218114976723@s.whatsapp.net":
        nombres = ["Baumann"]
        return randstr(nombres)

    else:
        return ""
#    random helper function
def randstr(arr):
    random1 = random.randint(0, (len(arr)-1))
    return arr[random1]

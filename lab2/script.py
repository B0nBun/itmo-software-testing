import config_reader
import consts
import logging
import time

import global_holder
from config_reader import get_prop
from sensors.hard import switch_relay
from gpiozero import Button
from telemetry import add_event_record

# НУЖНО БОЛЬШЕ КОММЕНТАРИЕВ

# Глобальные переменные
try:
    water_level_button = Button(config_reader.get_prop(consts.WATER_LEVEL_PIN), pull_up=True)
except Exception: # Нужно специализировать ошибки
    water_level_button = 1
max_numb_of_cycles = int(config_reader.get_prop(consts.CYCLES_HALF_TANK))

# Глобальные переменные
humidifier_records = []
max_humidifier_rate = int(config_reader.get_prop(consts.MAX_HUMIDIFIER_RATE))


def increase_water_cycles():
    # Не нравится такое форматирование
    config_reader.set_state_prop(consts.WATER_CYCLES_CURRENT,
                                 str(int(config_reader.get_state_prop(consts.WATER_CYCLES_CURRENT)) + 1))


def reset_water_cycles():
    config_reader.set_state_prop(consts.WATER_CYCLES_CURRENT, "0")


def get_water_cycles() -> int:
    return int(config_reader.get_state_prop(consts.WATER_CYCLES_CURRENT))


def set_humidifier_value(turned_on: bool) -> bool:
    if turned_on:
        increase_water_cycles()
        switch_relay.switch_relay(int(get_prop(consts.HUMIDIFIER_SWITCH_PIN)), turned_on)
        time.sleep(int(get_prop(consts.HUMIDIFIER_WORK_TIME)))
        switch_relay.switch_relay(int(get_prop(consts.HUMIDIFIER_SWITCH_PIN)), not turned_on)
    return turned_on


def enough_water() -> bool:
    if water_level_button.value == 0:
        return max_numb_of_cycles >= get_water_cycles()
    else:
        reset_water_cycles()
        return True


def is_humidifier_allowed() -> bool:
    one_hour_period = 3600
    now = time.time()
    return get_current_humidifier_rate_for_period(one_hour_period, now) < max_humidifier_rate


def get_current_humidifier_rate_for_period(period: int, now: float) -> int:
    return len(list(filter(lambda spray_time: now - spray_time < period, humidifier_records)))


def add_humidifier_cycle():
    # GLOBAL
    global humidifier_records
    humidifier_records.append(time.time())
    if len(humidifier_records) > 30:
        # humidifier_records = humidifier_records[-30:]
        humidifier_records = humidifier_records[::-1][:30][::-1]


def should_spray_happen(curr_humid: float, humid_max: float, humid_min: float) -> bool:
    deviation = get_humidity_deviation(curr_humid, humid_max, humid_min)
    is_enough_water = enough_water()
    is_humid_allowed = is_humidifier_allowed()
    one_hour_period = 3600
    logging.debug(
        f"last hour rate {get_current_humidifier_rate_for_period(one_hour_period, time.time())} water is {is_enough_water}, cycles {get_water_cycles()}"
    )
    return is_enough_water and is_humid_allowed and deviation < 0


def get_humidity_deviation(curr_humid: float, humid_max: float, humid_min: float) -> int:
    humid_max = float(humid_max)
    humid_min = float(humid_min)

    if humid_min > humid_max:
        humid_max, humid_min = humid_min, humid_max

    if curr_humid > humid_max:
        humid_deviation = 1
    elif curr_humid < humid_min:
        humid_deviation = -1
    else:
        humid_deviation = 0
    logging.debug('process_humidity humid_max = ' + str(humid_max) + ' humid_min = ' + str(humid_min) +
                  'current_humid = ' + str(curr_humid) + ' humid_deviation = ' + str(humid_deviation))
    return humid_deviation



def process_humidity(curr_humid: float, humid_max: float, humid_min: float):
    if humid_max is None or humid_min is None:
        logging.error("humid_max or humid_min is None. Cannot process humidity.")
        return
    try:
        spray = should_spray_happen(curr_humid, humid_max, humid_min)
        if spray:
            set_humidifier_value(True)
            add_event_record("humidifier", global_holder.current_state.temperature_hot,
                             global_holder.current_state.humidity_hot, global_holder.current_state.temperature_cold,
                             global_holder.current_state.humidity_cold)
            add_humidifier_cycle()
    except Exception as e: # Специализировать ошибку
        logging.error(
            msg=f"process_humidity curr_humid: {curr_humid}, humid_min: {humid_min}, " +
                f"humid_max: {humid_max} exception: {str(e)}",
            exc_info=True
        )

#!/bin/sh
tbboot
tbsql sys/tibero @create_user.sql
tail -f /dev/null
#!/bin/bash
mongo -u "admin" -p "secret" --authenticationDatabase "admin" "account" --eval "db.createUser({ user: 'account', pwd: 'secret', roles: [{ role: 'dbOwner', db: 'account' }] })"
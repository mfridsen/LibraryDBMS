#!/bin/bash

# Specify new author and contact
new_author="Mattias Fridsén, Johan Lund, Jesper Truedsson"
new_contact="matfir-1@student.ltu.se, ojaulz-2@student.ltu.se, jestru-2@student.ltu.se"

# Go through all .java files recursively in the current directory
find . -name "*.java" | while read file; do
    # Exclude files containing 'Test', 'RentalHandler', 'ItemHandler' or 'UserHandler' in their name
    if [[ $file != *"Test"* && $file != *"RentalHandler"* && $file != *"ItemHandler"* && $file != *"UserHandler"* ]]; then
        # Replace author and contact using sed command
        sed -i -r "s/(.*@author)(.*)/\1 $new_author/" "$file"
        sed -i -r "s/(.*@contact)(.*)/\1 $new_contact/" "$file"
    fi
done

#run chmod +x update_author_contact.sh to make it executable.
# run ./update_author_contact.sh to execute the script.
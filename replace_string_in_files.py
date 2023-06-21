import getopt
import os
import sys
import re


class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKCYAN = '\033[96m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def replace_string_in_files(file_pattern, regex, replace_string):
    file_counter = 0
    for current_path, folders, files in os.walk('.'):
        for file in files:
            f = os.path.join(current_path, file)
            if file_pattern in f:
                with open(f, 'rb') as fd:
                    filedata = fd.read().decode(errors='replace')
                if re.search(regex, filedata):
                    filedata = re.sub(regex, replace_string, filedata)
                    with open(f, 'w') as fd:
                        fd.write(filedata)
                        file_counter += 1
                        print("Updated file: {}".format(str(f)))
    print(f"{bcolors.OKGREEN} >>> DONE! Updated {file_counter} files in total.{bcolors.ENDC}")


def main(argv):
    replace = ''
    file_pattern = ''
    search_regex = ''
    opts, args = getopt.getopt(argv, "", ["replace=", "search_regex=", "file_pattern="])
    for opt, arg in opts:
        if opt == '-h':
            print('replace_string_in_files.py --r <replace> --s <search_regex> --f <file_pattern>')
            sys.exit()
        elif opt in ("-s", "--search_regex"):
            search_regex = arg
        elif opt in ("-r", "--replace"):
            replace = arg
        elif opt in ("-f", "--file_pattern"):
            file_pattern = arg
    print('Replace string: "{}"'.format(replace))
    print('Search Regex string: "{}"'.format(search_regex))
    print('File pattern: "{}"'.format(file_pattern))
    print('=' * 50)
    replace_string_in_files(file_pattern=file_pattern, regex=search_regex, replace_string=replace)


if __name__ == "__main__":
    main(sys.argv[1:])

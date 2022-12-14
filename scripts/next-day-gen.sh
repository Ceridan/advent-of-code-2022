#!/usr/bin/env bash

# This script generates data for the next day: code file, test file and download input.
#
# Usage:
# ./next-day-gen.sh 01

# Read args
if [ $# -eq 0 ]; then
    echo "You must provide day number"
    exit 1
fi

DAY=$1
ROOT_DIR=$(cd -- "$(dirname "$0")/.." >/dev/null 2>&1 || exit ; pwd -P)

# Create code file
code_filename="${ROOT_DIR}/src/advent_of_code_2022/day${DAY}.clj"
if [ -f "$code_filename" ]; then
    echo "File \"$code_filename\" already exists"
    exit 1
fi

cat <<EOF > "$code_filename"
(ns advent-of-code-2022.day${DAY}
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]))

(defn part1
  [data]
  nil)

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "${DAY}"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
EOF

# Create test file
test_filename="${ROOT_DIR}/test/advent_of_code_2022/day${DAY}_test.clj"
if [ -f "$test_filename" ]; then
    echo "File \"$test_filename\" already exists"
    exit 1
fi

cat <<EOF > "$test_filename"
(ns advent-of-code-2022.day${DAY}-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day${DAY} :refer [part1 part2]]))

(deftest part1-test
  (testing "Part 1"
    (is (= nil (part1 "abc")))))

(deftest part2-test
  (testing "Part 2"
    (is (= nil (part2 "def")))))
EOF

# Download input
input_filename="${ROOT_DIR}/resources/day${DAY}.txt"
if [ -f "$input_filename" ]; then
    echo "File \"input_filename\" already exists"
    exit 1
fi

cookies=$(cat "${ROOT_DIR}"/.env)
curl https://adventofcode.com/2022/day/$((DAY))/input -b "${cookies}"  -o "${input_filename}"

# Add to git
git add "${code_filename}" "${test_filename}" "${input_filename}"

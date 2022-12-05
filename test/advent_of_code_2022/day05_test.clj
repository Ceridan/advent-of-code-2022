(ns advent-of-code-2022.day05-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.string :as str]
            [advent-of-code-2022.day05 :refer [part1 part2]]))

(def example-stacks (str/split-lines "
    [D]
[N] [C]
[Z] [M] [P]
 1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2"))

(deftest part1-test
  (testing "Part 1"
    (is (= "CMZ" (part1 example-stacks)))))

(deftest part2-test
  (testing "Part 2"
    (is (= "MCD" (part2 example-stacks)))))

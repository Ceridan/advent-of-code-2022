(ns advent-of-code-2022.day02-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day02 :refer [part1 part2]]))

(def example-strategy [["A" "Y"]
                       ["B" "X"]
                       ["C" "Z"]])

(deftest part1-test
  (testing "Part 1"
    (is (= 15 (part1 example-strategy)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 12 (part2 example-strategy)))))

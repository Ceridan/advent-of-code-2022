(ns advent-of-code-2022.day17-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day17 :refer [part1 part2]]))

(def jet-example ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>")

(deftest part1-test
  (testing "Part 1"
    (is (= 3068 (part1 jet-example 2022)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 1514285714288 (part2 jet-example 1000000000000)))))

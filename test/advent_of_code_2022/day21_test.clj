(ns advent-of-code-2022.day21-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day21 :refer [part1 part2]]))

(def monkey-math-example ["root: pppw + sjmn"
                          "dbpl: 5"
                          "cczh: sllz + lgvd"
                          "zczc: 2"
                          "ptdq: humn - dvpt"
                          "dvpt: 3"
                          "lfqf: 4"
                          "humn: 5"
                          "ljgn: 2"
                          "sjmn: drzm * dbpl"
                          "sllz: 4"
                          "pppw: cczh / lfqf"
                          "lgvd: ljgn * ptdq"
                          "drzm: hmdt - zczc"
                          "hmdt: 32"])

(deftest part1-test
  (testing "Part 1"
    (is (= 152 (part1 monkey-math-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 301 (part2 monkey-math-example)))))

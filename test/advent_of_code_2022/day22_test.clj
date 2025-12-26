(ns advent-of-code-2022.day22-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day22 :refer [part1 part2]]))

(def monkey-map-example ["        ...#"
                         "        .#.."
                         "        #..."
                         "        ...."
                         "...#.......#"
                         "........#..."
                         "..#....#...."
                         "..........#."
                         "        ...#...."
                         "        .....#.."
                         "        .#......"
                         "        ......#."
                         ""
                         "10R5L5R10L4R5L5"])

(def cube-scheme {:side-size         4
                  :side-to-neighbors {1 {:N 2, :W 3, :S 4, :E 6}
                                      2 {:N 1, :W 6, :S 5, :E 3}
                                      3 {:N 1, :W 2, :S 5, :E 4}
                                      4 {:N 1, :W 3, :S 5, :E 6}
                                      5 {:N 4, :W 3, :S 2, :E 6}
                                      6 {:N 4, :W 5, :S 2, :E 1}}})

(deftest part1-test
  (testing "Part 1"
    (is (= 6032 (part1 monkey-map-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 5031 (part2 monkey-map-example cube-scheme)))))

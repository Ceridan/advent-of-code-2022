(defproject advent-of-code-2022 "0.1.0-SNAPSHOT"
  :description "Advent of Code 2022"
  :url "https://adventofcode.com/2022"
  :license {:name "MIT"
            :url  "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

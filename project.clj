(defproject subsystem "0.1.0-SNAPSHOT"
  :description "Component subsystems. Turn a system map into a component in a larger system."
  :url "http://github.com/tcsavage/subsystem"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/algo.generic "0.1.2"]
                 [com.stuartsierra/component "0.2.3"]]
  :plugins [[codox "0.8.12"]
            [jonase/eastwood "0.2.1"]]
  :codox {:defaults {:doc/format :markdown}
          :src-dir-uri "http://github.com/tcsavage/subsystem/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :eastwood {:linters [:all]})

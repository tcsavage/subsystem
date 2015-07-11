(defproject subsystem "0.1.0-SNAPSHOT"
  :description "Component subsystems."
  :url "http://github.com/tcsavage/subsystem"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/algo.generic "0.1.2"]
                 [com.stuartsierra/component "0.2.3"]]
  :plugins [[codox "0.8.12"]]
  :codox {:defaults {:doc/format :markdown}})

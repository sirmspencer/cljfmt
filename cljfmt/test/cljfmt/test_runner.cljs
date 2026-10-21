(ns cljfmt.test-runner
  (:require [cljfmt.core-test]
            [cljfmt.rules.split-keypairs-over-multiple-lines-test]
            [cljs.nodejs :as nodejs]
            [cljs.test :refer-macros [run-tests]]))

(nodejs/enable-util-print!)

(defmethod cljs.test/report [:cljs.test/default :end-run-tests] [m]
  (when (or (pos? (:fail m))
            (pos? (:error m)))
    (js/process.exit 1)))

(defn -main []
  (run-tests 'cljfmt.core-test
             'cljfmt.rules.split-keypairs-over-multiple-lines-test))

(set! *main-cli-fn* -main)

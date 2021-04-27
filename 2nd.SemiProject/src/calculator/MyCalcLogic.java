package calculator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyCalcLogic {
   
   MyCalcView mcv = null;
   
   MyCalcLogic(MyCalcView mcv) {
      this.mcv=mcv;
   }
      
   protected void clearAll() {
      setDisplayString("0");
      mcv.lastOperator = "0";
      mcv.lastNumber = 0;
      mcv.displayMode = mcv.INPUT_MODE;
      mcv.clearOnNextDigit = true;
   }
   
   // 숫자창에 입력하기
      public void addToDisplay(int i) {
         if (mcv.clearOnNextDigit) // clearAll 에서 true로 저장되어 있음
            setDisplayString(""); // jl_output에 저장
         
         String inputString = getDisplayString();// jl_output 에 저장된 값 가져오기 ""

         if (inputString.indexOf("0") == 0) { // 첫번째 입력한 숫자가 0이면
            inputString = inputString.substring(1); // 두번째 숫자부터 저장
         }

         if ((!inputString.contentEquals("0") || i > 0) && inputString.length() < mcv.MAX_INPUT_LENGTH) {
            setDisplayString(inputString + i); // 입력한 숫자가 0이 아니면
         }
         mcv.displayMode = mcv.INPUT_MODE;
         mcv.clearOnNextDigit = false;
      }

      // 사칙연산
      public double processLastOperator() throws Exception {
         double   result         = 0;
         double   numberInDisplay   = getNumberInDisplay();

         if (mcv.lastOperator.equals("/")) {
            if (numberInDisplay == 0)
               throw (new Exception());

            result = mcv.lastNumber / numberInDisplay;
         }

         if (mcv.lastOperator.equals("*")) {
            result = mcv.lastNumber * numberInDisplay;
         }

         if (mcv.lastOperator.equals("-")) {
            result = mcv.lastNumber - numberInDisplay;
         }

         if (mcv.lastOperator.equals("+")) {
            result = mcv.lastNumber + numberInDisplay;
         }

         return result;
      }

      protected void setDisplayString(String string) {// setDisplayString(X)
         mcv.jl_output.setText(string);
      }

      // 디스플레이에 나올 문자열 가져오기
      public String getDisplayString() {
         return mcv.jl_output.getText();
      }

      // 입력된 문자열 double 로 가져오기
      public double getNumberInDisplay() {
         String input = mcv.jl_output.getText();
         return Double.parseDouble(input);
      }

      public void processOperator(String string) {

         if (mcv.displayMode != mcv.ERROR_MODE) {
            double numberInDisplay = getNumberInDisplay();

            if (!mcv.lastOperator.equals("0")) {

               try {
                  double result = processLastOperator();
                  displayResult(result);
                  mcv.lastNumber = result;
               }
               catch (Exception e) {
               }
            }
            else {
               mcv.lastNumber = numberInDisplay;
            }
            mcv.clearOnNextDigit = true;
            mcv.lastOperator = string;
         }
      }

      // 결과값 출력
      public void displayResult(double result) {
         setDisplayString(Double.toString(result));
         mcv.lastNumber = result;
         mcv.displayMode = mcv.RESULT_MODE;
         mcv.clearOnNextDigit = true;
      }

      public void addPoint() {
         mcv.displayMode = mcv.INPUT_MODE;
         if (mcv.clearOnNextDigit)
            setDisplayString("");
         String inputString = getDisplayString();
         if (inputString.indexOf(".") < 0)
            setDisplayString(new String(inputString + "."));
      }

      public void sqrRoot() {
         double result = 0;

         if (mcv.displayMode != mcv.ERROR_MODE) {

            try {
               if (getDisplayString().indexOf("-") == 0)
                  displayError("Invalid input for function");

               result = Math.sqrt(getNumberInDisplay());
               displayResult(result);

            }
            catch (Exception ex) {
               displayError("영으로 나눌 수 없습니다.");
               mcv.displayMode = mcv.ERROR_MODE;
            }
         }
      }

      private void displayError(String error) {
         setDisplayString(error);
         mcv.lastNumber = 0;
         mcv.displayMode = mcv.ERROR_MODE;
         mcv.clearOnNextDigit = true;
      }

      public void processSignChange() {

         if (mcv.displayMode == mcv.INPUT_MODE) {
            String input = getDisplayString();

            if (input.length() > 0 && !input.equals("0")) {
               if (input.indexOf("-") == 0)
                  setDisplayString(input.substring(1));
               else
                  setDisplayString("-" + input);
            }
         }
      }

      public void backspace() {

         if (mcv.displayMode != mcv.ERROR_MODE) {
            setDisplayString(getDisplayString().substring(0,
                                 getDisplayString().length() - 1));
            if (getDisplayString().length() < 1)
               setDisplayString("0");
         }
      }

      public void processEquals() {
         double result = 0;

         if (mcv.displayMode != mcv.ERROR_MODE) {

            try {
               result = processLastOperator();
               displayResult(result);
            }
            catch (Exception e) {
               displayError("영으로 나눌 수 없습니다.");
            }
            mcv.lastOperator = "0";
         }
      }

}
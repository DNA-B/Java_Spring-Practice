import FirstComponent from "./FristComponent";
import SecondComponent from "./SecondComponent";
import ThirdComponent from "./ThirdComponent";
import FourthComponent from "./FourthComponent";
import { FifthComponent } from "./FristComponent";
import LearningJavaScript from "./LearningJavaScript";

export default function LearningComponent() {
  return (
    <div className="App">
      <FirstComponent />
      <SecondComponent />
      <ThirdComponent />
      <FourthComponent />
      <FifthComponent />
      <LearningJavaScript />
    </div>
  );
}

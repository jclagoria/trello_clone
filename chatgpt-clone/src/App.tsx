import {Header} from "./components/Header"
import {IntroSection} from "./components/IntroSection"
import {Footer} from "./components/Footer"
import {useState} from "react";
import {SingUp} from "./components/SignUp.tsx";

function App() {
    const [showSignUp, setShowSignUp] = useState(false);

    const handleSignUpClick = () => {
        setShowSignUp(true);
    }

  return (
    <>
        <Header onSignUpClick={handleSignUpClick} />

        {showSignUp ? <SingUp /> : <IntroSection />}
        <Footer />
    </>
  )
}

export default App

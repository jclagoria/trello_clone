import { BrowserRouter as Router, Route, Routes } from "react-router-dom"
import { Header } from "./components/Header"
import { HomePage } from "./components/HomePage"
import { SignUpPage } from "./components/SignUpPage"
import {Footer} from "./components/Footer.tsx";

function App() {
  return (
    <Router>
      <div>
        <Header />
        <Routes>
          <Route path="/" Component={HomePage} />
          <Route path="/signup" Component={SignUpPage}/>
        </Routes>
        <Footer />
      </div>
    </Router>
  )
}

export default App

import { BrowserRouter as Router, Route, Routes } from "react-router-dom"
import { Header } from "./components/Header"
import { HomePage } from "./components/HomePage"
import { SignUpPage } from "./components/SignUpPage"

function App() {
  return (
    <Router>
      <div>
        <Header />
        <Routes>
          <Route path="/" Component={HomePage} />
          <Route path="/signup" Component={SignUpPage}/>
        </Routes>
      </div>
    </Router>
  )
}

export default App

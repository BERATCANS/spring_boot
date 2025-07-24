import React, { Component } from "react";

class App extends Component {
  state = {
    students: []
  };

  async componentDidMount() {
    const response = await fetch('/students');
    const body = await response.json();
    this.setState({ students: body });
  }

  render() {
    const { students } = this.state;

    return (
        <div className="App">

          <header className="App-header">
            <h2>Öğrenci Listesi</h2>
            {students.map(student => (
                <div key={student._getId}>
                  {student.getName}
                </div>
            ))}
          </header>
        </div>
    );
  }
}

export default App;
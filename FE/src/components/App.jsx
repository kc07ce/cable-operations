import React, { Component } from 'react';
import '../styles/App.css'
import axios from 'axios';
import SearchBar from './SearchBar';
import CustStb from './CustStb';


class App extends React.Component {

    constructor(){
        super();
        this.searchBarRef = React.createRef();
        this.state={
            selectedObj:null,
            searchResp:[]
        }
    }

    onClickOutside = (event)=>{
        if(!this.searchBarRef.current.contains(event.target)){
            this.setState({
                searchResp:[]
            });
        }
    }

    displaySelectedResult = (obj)=>{
        this.setState({
            selectedObj:obj,
            searchResp:[]
        });
    }

    getCustStbDetails = (searchValue)=>{
        if(searchValue!=='')
            axios.get('http://192.168.29.91:8080/api/v1/cable/search', {params:{searchValue}})
                .then(response => {
                    this.setState({searchResp:response.data});
                })
                .catch(err => {console.log(err)});
        else
            this.setState({searchResp:[]});
    }

    render() { 
        return (<div onClick={this.onClickOutside} className='App'>
            <SearchBar  
            innerRef={this.searchBarRef} 
            updateSearch={this.getCustStbDetails.bind(this)} 
            searchResp={this.state.searchResp} 
            placeholder='Enter Name or STB No. or Account No...'
            displaySelectedResult={this.displaySelectedResult.bind(this)}
            />
            {this.state.selectedObj &&
                <CustStb data={this.state.selectedObj} />
            }
        </div>);
    }
}
 
export default App;
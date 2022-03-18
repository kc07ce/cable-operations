import React, { Component } from 'react';
import {BsSearch} from 'react-icons/bs';
import axios from 'axios';
import '../styles/App.css'


class SearchBar extends Component {

    constructor(props){
        super(props);
    }

    selectResult = (obj)=>{
        this.props.displaySelectedResult(obj);
    }

    getCustStbDetails = (event)=>{
        this.props.updateSearch(event.target.value);
    }

    render() { 
        return (
            
            <div ref={this.props.innerRef} className='search'>
                <div className='searchBar'>
                    <input onChange={this.getCustStbDetails} type="text" placeholder={this.props.placeholder}></input>
                    <div id='icon'><BsSearch/></div>
                </div>
                
                {this.props.searchResp.length !=0 &&
                    <div  id='searchResults'>
                            {this.props.searchResp.map(e=><p onClick={()=>this.selectResult(e)} className='searchResult' id={e.matchString}>{e.matchString}</p>)}
                    </div>
                }
                
            </div>
            
        );
    }
}
 
export default SearchBar;
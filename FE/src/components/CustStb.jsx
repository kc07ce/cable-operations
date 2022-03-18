import React, { Component } from 'react';
import {GiJoin} from 'react-icons/gi';
import '../styles/CustStb.css'

class CustStb extends Component {
    
    constructor(props){
        super(props);
        this.state={
            custStbDetail:this.props.data
        }
    }

    getLable = (key)=>{
        const mapping={
            "name":"Name",
            "doorNo":"Door No.",
            "phoneNo":"Phone No.",
            "balance":"Balance in Rs",
            "accountNo": "Account No.",
            "stbNo": "STB No.",
            "stbType": "STB Type",
            "remarks": "Remarks"
        }
        return mapping[key];
    }

    filterObj = (e)=>{
        return (e!=="cid" && e!=="stbId" && e!="stbUrl" && e!="remarks");
    }

    modifyDetails=(event, key)=>{
        console.log(event);
        console.log(key)
    }

    render() { 
        
        const customer = this.props.data.customer;
        const stb = this.props.data.stb;
        return (<div className='custStb'>
            <div className='info'>
                {customer ?
                    Object.keys(customer).filter(this.filterObj).map(e=>(
                        <div>
                            <p>{this.getLable(e)}</p>
                            <input 
                            type='text' 
                            id='customer' 
                            value={customer[e]!=null?customer[e]:''} 
                            onChange={()=>this.modifyDetails(e)} 
                            disabled/>
                        </div>
                    )):<p>No Customer assigned</p>
                }
            </div>
            <h1>customer</h1>
            <div id='joinIcon'>
                <GiJoin/>
            </div>
            <h1>stb</h1>
            <div className='info'>
                {stb ?
                    Object.keys(stb).filter(this.filterObj).map(e=>(
                        <div>
                            <p>{this.getLable(e)}</p>
                            <input 
                            type='text' 
                            id='customer' 
                            value={stb[e]!=null?stb[e]:''} 
                            onChange={()=>this.modifyDetails(e)} 
                            disabled/>
                        </div>
                    )):<p>No STB assigned</p>
                }
            </div>
        </div>);
    }
}
 
export default CustStb;
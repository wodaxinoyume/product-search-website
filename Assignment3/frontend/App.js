import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import { MdSearch, MdClearAll } from 'react-icons/md';
import Autocomplete from '@mui/material/Autocomplete';
import AppBottom from './AppBottom';
import './App.css'

const App = () => {
    const zipcodeRegex = /[^0-9]/;
    const [isOtherSelected, setIsOtherSelected] = useState(false);
    const [suggestions, setSuggestions] = useState([]);
    const [location, setLocation] = useState("");
    const [zipcode, setZipcode] = useState("");
    const [keyword, setKeyword] = useState("");
    const [category, setCategory] = useState("0");
    const [New, setNew] = useState(false);
    const [used, setUsed] = useState(false);
    const [unspecified, setUnspecified] = useState(false);
    const [pickup, setPickup] = useState(false);
    const [shipping, setShipping] = useState(false);
    const [distance, setDistance] = useState("10");
    const [result, setResult] = useState("");
    const [loading, setLoading] = useState(false);
    const [keywordError, setKeywordError] = useState("");
    const [zipcodeError, setZipcodeError] = useState("");
    const [autocompleteVisible, setAutocompleteVisible] = useState(false);
    const [clearNum, setClearNum] = useState(0);

    useEffect(() => {
        fetchPostal();
    }, []);

    function fetchPostal() {
        fetch("https://ipinfo.io/json?token=28bfb37bda3238")
            .then((response) => response.json())
            .then((data) => {
                setZipcode(data.postal);
                // setLocation(data.postal); // for debug purpose
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    }

    const handleRadioChange = (event) => {
        setIsOtherSelected(event.target.value === 'Other');
        setLocation('');
        setZipcode('');
        if (event.target.value !== 'Other') {
            setZipcodeError('');
        }
        // call ipinfo api to get current zipcode
        if (event.target.value !== 'Other') {
            fetchPostal();
        }
    };

    const handleLocationChange = (event) => {
        // console.log("before: " + location);
        setLocation(event.target.value);
        setZipcode(event.target.value);
        // console.log("after: " + location);
        // call geonames api to autocomplete
        // const url = `http://localhost:8080/IPSuggest?IP=${event.target.value}`;
        const url = `https://rugged-shuttle-402803.wn.r.appspot.com/IPSuggest?IP=${event.target.value}`;
        // const url = `http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=${event.target.value}&maxRows=5&username=wodaxinoyume&country=US`

        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                const postalCodes = data.postalCodes.map((item) => item.postalCode);
                setSuggestions(postalCodes);
                console.log(postalCodes)
            })
            .catch((error) => {
                setSuggestions([]);
            });
    }

    const handleZipcodeBlur = () => {
        setTimeout(() => {
            setAutocompleteVisible(false);
        }, 200);
        if (location.trim() === "") {
            setZipcodeError("Please enter a zipcode.");
        }
    }

    const handleZipcodeFocus = () => {
        setAutocompleteVisible(true);
        if (location.trim() === "") {
            setZipcodeError("");
        }
    }

    const handleKeywordChange = (event) => {
        setKeyword(event.target.value);
    }

    const handleKeywordBlur = () => {
        if (keyword.trim() === "") {
            setKeywordError("Please enter a keyword.");
        }
    }

    const handleKeywordFocus = () => {
        if (keyword.trim() === "") {
            setKeywordError("");
        }
    }

    const handleSearch = (event) => {
        setLoading(true);

        // const endpoint = "http://localhost:8080/search";
        const endpoint = "https://rugged-shuttle-402803.wn.r.appspot.com/search";

        const params = {
            "keywords": keyword,
            "postalcode": zipcode,
            "distance": distance,
            "category": category,
        }

        if (New) {
            params["new"] = "true";
        }

        if (used) {
            params["used"] = "true";
        }

        if (pickup) {
            params["localPickup"] = "true";
        }

        if (shipping) {
            params["freeShipping"] = "true";
        }

        const url = new URL(endpoint);
        url.search = new URLSearchParams(params).toString();
        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                setResult(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
                setLoading(false);
            });
    }

    const handleClear = (event) => {
        setKeyword("");
        setCategory("0");
        setNew(false);
        setUsed(false);
        setUnspecified(false);
        setPickup(false);
        setShipping(false);
        setDistance("10");
        setIsOtherSelected(false);
        setResult("");
        setZipcode("");
        setLocation("");
        setSuggestions([]);
        fetchPostal();
        setKeywordError("");
        setZipcodeError("");
        setClearNum(clearNum + 1);
    }

    const handleSuggestionClick = (item) => {
        setLocation(item);
        setZipcode(item);
        const url = `https://rugged-shuttle-402803.wn.r.appspot.com/IPSuggest?IP=${item}`;

        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                const postalCodes = data.postalCodes.map((item) => item.postalCode);
                setSuggestions(postalCodes);
                console.log(postalCodes)
            })
            .catch((error) => {
                setSuggestions([]);
            });
    }

    return (
        <>
            <Container className='blackBox'>
            <Container style={{ maxWidth: '600px'}}>
                    <h3 className="mt-4 mb-4">Product Search</h3>
                    <Form>
                        <Form.Group as={Row} style={{ marginBottom: '15px'}}>
                            <Form.Label column className="mr-1">
                                Keyword<span style={{color: 'red'}}>*</span>
                            </Form.Label>
                            <Col sm="9">
                                <Form.Control 
                                value={keyword} 
                                onChange={handleKeywordChange} 
                                onBlur={handleKeywordBlur}
                                onFocus={handleKeywordFocus}
                                isInvalid={!!keywordError}
                                placeholder="Enter Product Name(eg. iPhone 8)"/>
                                <Form.Control.Feedback type="invalid">
                                    {keywordError}
                                </Form.Control.Feedback>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} className="align-items-center"  style={{ marginBottom: '15px'}}>
                            <Form.Label column sm="3">
                                Category
                            </Form.Label>
                            <Col sm="4">
                                <Form.Select value={category} onChange={e => setCategory(e.target.value)}>
                                    <option value="0">All Categoris</option>
                                    <option value="550">Art</option>
                                    <option value="2984">Baby</option>
                                    <option value="267">Books</option>
                                    <option value="11450">Clothing, Shoes & Accessories</option>
                                    <option value="58058">Computers, Tablets & Networking</option>
                                    <option value="26395">Health & Beauty</option>
                                    <option value="11233">Music</option>
                                    <option value="1249">Video Games & Consoles</option>
                                </Form.Select>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} className="align-items-center" style={{ marginBottom: '15px'}}>
                            <Form.Label column className="mr-1">
                                Condition
                            </Form.Label>
                            <Col sm="9" className='d-flex'>
                                <Form.Check checked={New} onChange={() => setNew(!New)} label='New' style={{marginRight: '15px'}} />
                                <Form.Check checked={used} onChange={() => setUsed(!used)} label='Used' style={{marginRight: '15px'}} />
                                <Form.Check checked={unspecified} onChange={() => setUnspecified(!unspecified)} label='Unspecified'/>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} className="align-items-center" style={{ marginBottom: '15px'}}>
                            <Form.Label column className="mr-1">Shipping Options</Form.Label>
                            <Col sm="9" className='d-flex'>
                                <Form.Check checked={pickup} onChange={() => setPickup(!pickup)} label='Local Pickup' style={{marginRight: '15px'}} />
                                <Form.Check checked={shipping} onChange={() => setShipping(!shipping)} label='Free Shipping'/>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} style={{ marginBottom: '15px'}}>
                            <Form.Label column sm="3">
                                Distance (Miles)
                            </Form.Label>
                            <Col sm="4">
                                <Form.Control value={distance} onChange={e => setDistance(e.target.value)} type="number" defaultValue="10"/>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} style={{ marginBottom: '15px'}}>
                            <Form.Label column className="mr-1">
                                From<span style={{color: 'red'}}>*</span>
                            </Form.Label>
                            <Col sm="9">
                                <Form.Check 
                                type="radio" 
                                name="zipbox"
                                value='Current' 
                                label='Current Location' 
                                checked={!isOtherSelected}
                                onChange={handleRadioChange} 
                                style={{marginRight: '15px'}} />

                                <Form.Check
                                type="radio" 
                                name="zipbox"
                                value='Other' 
                                label='Other. Please specify zip code:' 
                                // label={
                                //     <Col>
                                //         <div className="flex-container">
                                //             <span className='space'>Other. Please specify zip</span>
                                //             <span>code:</span>
                                //         </div>
                                //     </Col>
                                // }
                                checked={isOtherSelected}
                                onChange={handleRadioChange}
                                style={{marginRight: '15px'}} />

                                <div className='autocomplete'>
                                    <Form.Control
                                    value={location}
                                    onChange={handleLocationChange}
                                    onBlur={handleZipcodeBlur}
                                    onFocus={handleZipcodeFocus}
                                    isInvalid={!!zipcodeError && isOtherSelected}
                                    readOnly={!isOtherSelected}
                                    maxLength={5}
                                    style={{marginTop: '10px'}}/>

                                    {isOtherSelected && location.trim().length !== 0 && autocompleteVisible &&
                                    <div className='autocomplete-items'>
                                        {suggestions.map((item, index) => (
                                            <div key={index} onClick={() => handleSuggestionClick(item)}>{item}</div>
                                        ))}
                                    </div>}

                                    <Form.Control.Feedback type="invalid">{zipcodeError}</Form.Control.Feedback>
                                </div>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row}>
                            <Col className='d-flex'>
                                <Button 
                                variant="light" 
                                className="align-items-center"
                                onClick={handleSearch}
                                disabled={keyword.trim()==="" || zipcode.length!==5 || zipcodeRegex.test(zipcode)} 
                                style={{display: 'flex', marginRight: '25px'}}>
                                    <MdSearch style={{fontSize: '25px'}} />Search
                                </Button>
                                <Button 
                                variant="light" 
                                className="align-items-center" 
                                onClick={handleClear}
                                style={{display: 'flex'}}>
                                    <MdClearAll style={{fontSize: '28px'}} />Clear
                                </Button>
                            </Col>
                        </Form.Group>
                    </Form>
                </Container>
            </Container>

            <AppBottom loading={loading} result={result} key={clearNum} />
        </>
    );
};

export default App;

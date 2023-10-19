import React, { useState } from 'react';
import { Container, Form, Row, Col, Card, Button } from 'react-bootstrap';
import './App.css';

const DetailSimilarCard = (detailJson3) => {
    const [name, setName] = useState("0");
    const [sort, setSort] = useState("0");

    const items = detailJson3.detailJson3.detailJson.getSimilarItemsResponse.itemRecommendations.item;

    let hideButton = false;
    if (items.length <= 5) {
        hideButton = true;
    }

    const [moreItem, setMoreItem] = useState(true);

    const tableData = items.map(item => {
        const imageUrl = item.imageURL;
        const itemUrl = item.viewItemURL;
        const title = item.title;
        
        const price = parseFloat(item.buyItNowPrice.__value__);
        const shippingPrice = parseFloat(item.shippingCost.__value__);
        
        const timeLeft = item.timeLeft;
        const timeLeftMatches = timeLeft.match(/P(\d+)D/);
        const days = parseInt(timeLeftMatches[1]);
        
        return { imageUrl, itemUrl, title, price, shippingPrice, days };
    });

    const handleNumberChange = () => {
        setMoreItem(!moreItem);
    }

    let dataAfterSort = moreItem ? tableData.slice(0, 5) : tableData;

    if (name === "0") {
        dataAfterSort = moreItem ? tableData.slice(0, 5) : tableData;
    } else if (name === "1") {
        if (sort === "0") {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => a.title.localeCompare(b.title));
        } else {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => b.title.localeCompare(a.title));
        }
    } else if (name === "2") {
        if (sort === "0") {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => a.days - b.days);
        } else {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => b.days - a.days);
        }
    } else if (name === "3") {
        if (sort === "0") {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => a.price - b.price);
        } else {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => b.price - a.price);
        }
    } else if (name === "4") {
        if (sort === "0") {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => a.shippingPrice - b.shippingPrice);
        } else {
            dataAfterSort = dataAfterSort.slice().sort((a, b) => b.shippingPrice - a.shippingPrice);
        }
    }

    return (
        <>
            <Container style={{marginTop: "15px", marginBottom: "15px"}}>
                <Row>
                    <Col sm="2">
                        <Form.Select value={name} onChange={e => setName(e.target.value)}>
                            <option value="0">Default</option>
                            <option value="1">Product Name</option>
                            <option value="2">Days Left</option>
                            <option value="3">Price</option>
                            <option value="4">Shipping Cost</option>
                        </Form.Select>
                    </Col>
                    <Col sm = "2">
                        <Form.Select value={sort} onChange={e => setSort(e.target.value)} disabled={name === "0"}>
                            <option value="0">Ascending</option>
                            <option value="1">Descending</option>
                        </Form.Select>
                    </Col>
                </Row>
            </Container>
            <Container>
                {dataAfterSort.map((item, index) => (
                    <Card key={index} style={{ marginBottom: '20px', backgroundColor: '#181d1f' }}>
                        <Row>
                            <Col lg={2} sm={12} style={{ display: 'flex', alignItems: 'center' }}>
                                <div style={{ margin: '10px', width: '200px', height: '200px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                                    <a href={item.imageUrl} target="_blank">
                                    <Card.Img src={item.imageUrl} style={{ objectFit: 'cover', width: '100%', height: '100%' }} />
                                    </a>
                                </div>
                            </Col>
                            <Col lg={10} sm={12} style={{ flex: 1, padding: '10px', alignItems: 'start' }}>
                                <div style={{margin: "10px"}}>
                                    <span style={{ color: 'blue' }}>{item.title}</span><br />
                                    <span style={{ color: 'green' }}>Price: ${item.price.toFixed(2)}</span><br />
                                    <span style={{ color: 'orange' }}>Shipping Price: ${item.shippingPrice.toFixed(2)}</span><br />
                                    <span style={{ color: 'white' }}>Days Left: {item.days}</span>
                                </div>
                            </Col>
                        </Row>
                    </Card>
                ))}
            </Container>
            <div style={{display: "flex", justifyContent: "center"}}>
                {!hideButton && <Button variant='dark' onClick={handleNumberChange}>{moreItem ? "Show more" : "Show less"}</Button>}
            </div>
        </>    
    );
}

export default DetailSimilarCard;
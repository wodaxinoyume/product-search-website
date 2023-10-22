import React, { useEffect, useState } from 'react';
import { Container, Button, ProgressBar, Tabs, Tab, Row, Col, Image } from 'react-bootstrap';
import DetailProduct from './DetailProduct';
import DetailShipping from './DetailShipping';
import DetailSeller from './DetailSeller';
import DetailSimilar from './DetailSimilar';
import DetailPhoto from './DetailPhoto';
import FavoriteRenderer from './FavoriteRenderer';
import './App.css';

const AppDetail = ({detail, setDetailPage, needCall, setNeedCall, detailJson, setDetailJson, detailJson2, setDetailJson2, detailJson3, setDetailJson3, wishlistId, setWishlistId}) => { 
    useEffect(() => {
        if (needCall) {
            handleDetailSearch();
            // handlePhotoSearch();
            handleSimilarSearch();
            setNeedCall(false);
        }
    });

    const [loading, setLoading] = useState(false);
    
    const [activeKey, setActiveKey] = useState("Product");

    const handleNavClick = (key) => {
        setActiveKey(key);
    }

    const handleDetailSearch = () => {
        setLoading(true);

        const endpoint = "http://localhost:8080/detail";

        const params = {
            "itemId": detail[1],
        }

        const url = new URL(endpoint);
        url.search = new URLSearchParams(params).toString();
        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                setDetailJson(data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
                setLoading(false);
            });
    }

    const handlePhotoSearch = () => {
        const endpoint = "http://localhost:8080/photo";

        const params = {
            "productTitle": detail[0],
        }

        const url = new URL(endpoint);
        url.search = new URLSearchParams(params).toString();
        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                setDetailJson2(data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    }

    const handleSimilarSearch = () => {
        const endpoint = "http://localhost:8080/similar";

        const params = {
            "itemId": detail[1],
        }

        const url = new URL(endpoint);
        url.search = new URLSearchParams(params).toString();
        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                setDetailJson3(data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });        
    }

    const handleListChange = () => {
        setDetailPage(false);
    }

    let content;
    if (activeKey === "Product") {
        content = <DetailProduct detailJson={detailJson} />;
    } else if (activeKey === "Photos") {
        content = <DetailPhoto detailJson={detailJson2} />;
    } else if (activeKey === "Shipping") {
        content = <DetailShipping info={detail[2]} detailJson={detail[4]}/>;
    } else if (activeKey === "Seller") {
        content = <DetailSeller info={detail[3]} detailJson={detail[5]}/>;
    } else {
        content = <DetailSimilar detailJson={detailJson3} />;
    } 

    const handleFacebookRec = () => {
        const productName = detail[0];
        const price = detail[7];
        const link = detail[6];

        const shareMessage = `Buy ${productName} at ${price} from ${link} below.`;

        const facebookShareUrl = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(link)}&quote=${encodeURIComponent(shareMessage)}`;
        window.open(facebookShareUrl, '_blank');
    }

    return (
        <>
            {loading ? <ProgressBar animated now={60} /> : (
                <>
                    <h3 style={{textAlign: "center"}}>{detail[0]}</h3>
                    <Container>
                        <Row>
                            <Col xs={10} className="d-flex justify-content-start">
                                <Button 
                                    variant="outline-secondary" 
                                    onClick={handleListChange}
                                    className='listButton'>
                                    {'< '}List
                                </Button>
                            </Col>
                            <Col xs={2} className="d-flex justify-content-end align-items-end">
                                <a onClick={handleFacebookRec} style={{cursor: "pointer"}}>
                                    <img style={{width: "40px", height: "40px"}} src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Facebook_icon_2013.svg/450px-Facebook_icon_2013.svg.png?20161223201621" />
                                </a>
                                <FavoriteRenderer props={detail[8]} wishlistId={wishlistId} setWishlistId={setWishlistId} />
                            </Col>
                        </Row>
                    </Container>
                    <Tabs activeKey={activeKey} onSelect={handleNavClick} className="d-flex justify-content-end">
                        <Tab eventKey="Product" title="Product" className='myTab' />
                        <Tab eventKey="Photos" title="Photos" className='myTab' />
                        <Tab eventKey="Shipping" title="Shipping" className='myTab' />
                        <Tab eventKey="Seller" title="Seller" className='myTab' />
                        <Tab eventKey="Similar" title="Similar Products" className='myTab' />
                    </Tabs>
                    <>{content}</>
                </>
            )}
        </>    
    );
};

export default AppDetail;
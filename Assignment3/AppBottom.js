import React, { useEffect, useState, useMemo } from 'react';
import { Container, ProgressBar, Nav } from 'react-bootstrap';
import AppResult from './AppResult';
import AppNoRecords from './AppNoRecords';
import AppWishlist from './AppWishlist';
import AppDetail from './AppDetail';
import './App.css';

const AppBottom = ({loading, result}) => {
    // when another search is triggered, reinitailized the state
    useEffect(() => {
        handleDBIDSearch();
        setDetail(null);
        setDetailPage(false);
        setActiveKey('results');
        setDetailJson(null);
        setDetailJson2(null);
        setDetailJson3(null);
        setPage(1);
    }, [result]);

    const [wishlistId, setWishlistId] = useState(null);

    const handleDBIDSearch = () => {
        const endpoint = "http://localhost:8080/findAllId";

        const url = new URL(endpoint);

        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                const idArray = data.data.map(item => item._id);
                setWishlistId(idArray);
                console.log(wishlistId)
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    }

    const [activeKey, setActiveKey] = useState('results');
    
    // record whether a detail search is triggered. If it is, then remember the itemID
    const [detail, setDetail] = useState(null);

    // record whether current page is a detail page or not
    const [detailPage, setDetailPage] = useState(false);

    // whether to call the backend to get the json for detailpage
    const [needCall, setNeedCall] = useState(false);

    // store the previous json for the detailpage
    const [detailJson, setDetailJson] = useState(null);
    const [detailJson2, setDetailJson2] = useState(null);
    const [detailJson3, setDetailJson3] = useState(null);

    const [page, setPage] = useState(1);

    const handleNavClick = (key) => {
        setActiveKey(key);
        setDetailPage(false);
    };

    const contentToRender = useMemo(() => {
        if (loading) {
            return <ProgressBar animated now={60} />;
        }

        if (activeKey === "results") {
            if (detailPage) {
                return <AppDetail 
                        detail={detail} 
                        setDetailPage={setDetailPage}
                        needCall={needCall}
                        setNeedCall={setNeedCall} 
                        detailJson={detailJson} 
                        setDetailJson={setDetailJson}
                        detailJson2={detailJson2} 
                        setDetailJson2={setDetailJson2}
                        detailJson3={detailJson3} 
                        setDetailJson3={setDetailJson3}
                        wishlistId={wishlistId}
                        setWishlistId={setWishlistId} />;
            } else if (result === "") {
                return null;
            } else if (!result?.findItemsAdvancedResponse?.[0]?.searchResult?.[0]) {
                return <AppNoRecords />;
            } else if (result.findItemsAdvancedResponse[0].searchResult[0]["@count"] === "0") {
                return <AppNoRecords />;
            } else {
                return <AppResult 
                        result={result} 
                        detail={detail} 
                        setDetail={setDetail} 
                        setDetailPage={setDetailPage} 
                        setNeedCall={setNeedCall}
                        page={page}
                        setPage={setPage}
                        wishlistId={wishlistId}
                        setWishlistId={setWishlistId} />;
            }
        } else {
            if (detailPage) {
                return <AppDetail 
                        detail={detail} 
                        setDetailPage={setDetailPage}
                        needCall={needCall}
                        setNeedCall={setNeedCall} 
                        detailJson={detailJson} 
                        setDetailJson={setDetailJson}
                        detailJson2={detailJson2} 
                        setDetailJson2={setDetailJson2}
                        detailJson3={detailJson3} 
                        setDetailJson3={setDetailJson3}
                        wishlistId={wishlistId}
                        setWishlistId={setWishlistId} />;
            } else {
                return <AppWishlist 
                detail={detail} 
                setDetail={setDetail}
                setDetailPage={setDetailPage}
                setNeedCall={setNeedCall}
                wishlistId={wishlistId}
                setWishlistId={setWishlistId} />;
            }
        }
    }, [loading, activeKey, detailPage, result, detail, setDetail, setDetailPage, needCall, setNeedCall, 
        detailJson, setDetailJson, detailJson2, setDetailJson2, detailJson3, setDetailJson3, page, setPage,
        wishlistId, setWishlistId]);

    return (
        <>
            <Container style={{display: "flex", justifyContent: "center", marginTop: "20px"}}>
                <Nav variant="pills" activeKey={activeKey} onSelect={handleNavClick}>
                    <Nav.Item>
                        <Nav.Link className='myNavPill' eventKey="results">Results</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link className='myNavPill' eventKey="wishlist">Wish List</Nav.Link>
                    </Nav.Item>
                </Nav>
            </Container>

            <Container style={{marginTop: "10px"}}>
                {contentToRender}
            </Container>
        </>
    );
}

export default AppBottom;
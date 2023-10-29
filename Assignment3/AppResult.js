import React, { useState, useRef, useEffect } from 'react';
import { Container, Button, Pagination } from 'react-bootstrap';
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import FavoriteRenderer from './FavoriteRenderer';
import './App.css';

const AppResult = ({result, detail, setDetail, setDetailPage, setNeedCall, page, setPage, wishlistId, setWishlistId}) => {
    const gridRef = useRef();
    const wishRef = useRef(wishlistId);

    const handleDetailSearch = (val) => {
        setDetail(val);
        setDetailPage(true);
        setNeedCall(true);
    }

    const handleDetailChange = () => {
        setDetailPage(true);
    }

    // proccess the data to present in the AgGridReact
    const imageRenderer = (props) => {
        return (
            <Container style={{display: "flex", justifyContent: "center", alignItems: "center", height: "200px"}}>
                <a href={props.value} target="_blank">
                    <img src={props.value} width="auto" height="150px" />
                </a>
            </Container>
        );
    }

    const titleRenderer = (props) => {
        return (
            <a title={props.value[0]} className='itemTitle' onClick={() => handleDetailSearch(props.value)} >
                {props.value[0] ? props.value[0] : "default title"}
            </a>
        );
    }

    const FavoriteIconRenderer = (val) => {
        return <FavoriteRenderer props={val.value} wishlistId={wishlistId} setWishlistId={setWishlistId} wishRef={wishRef}/>
    }

    const [columnDefs] = useState([
        { field: "#", width: "60px", suppressMovable: true },
        { field: "Image", width: "200px", cellRenderer: imageRenderer, suppressMovable: true },
        { field: "Title", width: "490px", cellRenderer: titleRenderer, suppressMovable: true },
        { field: "Price", width: "120px", suppressMovable: true },
        { field: "Shipping", width: "150px", suppressMovable: true },
        { field: "Zip", width: "130px", suppressMovable: true },
        { field: "Favorite", width: "120px", cellRenderer: FavoriteIconRenderer, suppressMovable: true }
    ]);

    const items = result.findItemsAdvancedResponse[0].searchResult[0].item;

    const itemsNum = items.length;

    const pageNum = Math.ceil(itemsNum / 10);

    const formatPrice = (price) => {
        if (!price) {
            return "";
        }
        return `$${parseFloat(price).toFixed(2)}`;
    }

    const formatShipping = (shippingCost) => {
        if (!shippingCost) {
            return "";
        }
        const cost = parseFloat(shippingCost);
        return cost === 0 ? "Free Shipping" : `$${cost.toFixed(2)}`;
    };

    const defaultImage = "https://www.bobswatches.com/rolex-blog/wp-content/uploads/2017/01/ebay-inventory-management-post.jpg";

    const tableData = items.map((item, index) => ({
        "#": index + 1,
        "Image": item.galleryURL[0] || defaultImage,
        "Title": [item.title?.[0], item.itemId[0], item.shippingInfo, item.sellerInfo, item.returnsAccepted, item.storeInfo, item.viewItemURL, formatPrice(item?.sellingStatus?.[0]?.convertedCurrentPrice[0].__value__),
                    [
                        item.galleryURL[0],
                        [item.title[0], item.itemId[0], item.shippingInfo, item.sellerInfo, item.returnsAccepted, item.storeInfo, item.viewItemURL, formatPrice(item?.sellingStatus?.[0]?.convertedCurrentPrice[0].__value__)],
                        formatPrice(item?.sellingStatus?.[0]?.convertedCurrentPrice?.[0]?.__value__),
                        formatShipping(item?.shippingInfo?.[0]?.shippingServiceCost?.[0]?.__value__),
                    ],],
        "Price": formatPrice(item?.sellingStatus?.[0]?.convertedCurrentPrice?.[0]?.__value__),
        "Shipping": formatShipping(item?.shippingInfo?.[0]?.shippingServiceCost?.[0]?.__value__),
        "Zip": item?.postalCode?.[0] ? item?.postalCode[0] : "",
        "Favorite": [
            item.galleryURL[0],
            [item.title[0], item.itemId[0], item.shippingInfo, item.sellerInfo, item.returnsAccepted, item.storeInfo, item.viewItemURL, formatPrice(item?.sellingStatus?.[0]?.convertedCurrentPrice[0].__value__)],
            formatPrice(item?.sellingStatus?.[0]?.convertedCurrentPrice[0].__value__),
            formatShipping(item?.shippingInfo?.[0]?.shippingServiceCost[0].__value__),
                    ],
    }));

    const [rowData] = useState(tableData);

    const gridOptions = {
        domLayout: 'autoHeight',
        suppressDragLeaveHidesColumns: true,
    };

    const getRowHeight = () => {
        return 200;
    };

    const handlePageChange = (num) => {
        setPage(num);
        gridRef.current.api.paginationGoToPage(num - 1);
    }

    const getRowStyle = (params) => {
        if (detail) {
            if (params.data.Favorite[1][1] === detail[1]) {
                return { background: 'grey' };
            }
        }
        return null;
    };

    return (
        <>
            <Container className='detailButtonBox'>
                <Button 
                variant="outline-secondary" 
                disabled={detail == null}
                onClick={handleDetailChange}
                className='detailButton'>
                    Detail{' >'}
                </Button>
            </Container>
            <Container className="ag-theme-alpine-dark" style={{ maxWidth: 1300 }}>
                <AgGridReact
                ref={gridRef}
                pagination="true"
                paginationPageSize="10"
                suppressPaginationPanel={true}
                onGridReady={(params) => { params.api.paginationGoToPage(page - 1); }}
                getRowHeight={getRowHeight}
                getRowStyle={getRowStyle}
                gridOptions={gridOptions}
                rowData={rowData} 
                columnDefs={columnDefs} />
            </Container>
            <Pagination style={{display: "flex", justifyContent: "center", marginTop: "10px"}}>
                <Pagination.First disabled={page===1} onClick={() => handlePageChange(1)}/>
                <Pagination.Prev disabled={page===1} onClick={() => handlePageChange(page - 1)}>Previous</Pagination.Prev>
                {<Pagination.Item onClick={() => handlePageChange(1)} active={page===1}>{1}</Pagination.Item>}
                {itemsNum > 10 && <Pagination.Item onClick={() => handlePageChange(2)} active={page===2}>{2}</Pagination.Item>}
                {itemsNum > 20 && <Pagination.Item onClick={() => handlePageChange(3)} active={page===3}>{3}</Pagination.Item>}
                {itemsNum > 30 && <Pagination.Item onClick={() => handlePageChange(4)} active={page===4}>{4}</Pagination.Item>}
                {itemsNum > 40 && <Pagination.Item onClick={() => handlePageChange(5)} active={page===5}>{5}</Pagination.Item>}
                <Pagination.Next disabled={page===pageNum} onClick={() => handlePageChange(page + 1)}>Next</Pagination.Next>
                <Pagination.Last disabled={page===pageNum}  onClick={() => handlePageChange(pageNum)}/>
            </Pagination>
        </>
    ); 
};

export default AppResult;
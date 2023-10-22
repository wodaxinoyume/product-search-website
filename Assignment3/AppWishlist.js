import React, { useEffect, useState } from 'react';
import { Container, Button } from 'react-bootstrap';
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import AppNoRecords from './AppNoRecords';
import FavoriteRenderer from './FavoriteRendererInWishlist';
import './App.css';

const AppWishlist = ({detail, setDetail, setDetailPage, setNeedCall, wishlistId, setWishlistId}) => {
    
    const [deleteId, setDeleteId] = useState(null);

    const [dbResult, setDbResult] = useState(null);

    useEffect(() => {
        handleDBSearch();
    }, [deleteId]);

    const handleDBSearch = () => {
        const endpoint = "http://localhost:8080/findAll";

        const url = new URL(endpoint);

        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                setDbResult(data.data);
                console.log(data.data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    }

    return (
        dbResult === null ? <AppNoRecords /> : (
            <div key={dbResult}>
                <AppWishlistTable 
                detail={detail} 
                setDetail={setDetail} 
                setDetailPage={setDetailPage} 
                setNeedCall={setNeedCall} 
                result={dbResult}
                wishlistId={wishlistId}
                setWishlistId={setWishlistId}
                setDeleteId={setDeleteId}
                deleteId={deleteId} />
            </div>
        )
    );
};

const AppWishlistTable = ({detail, setDetail, setDetailPage, setNeedCall, result, wishlistId, setWishlistId, setDeleteId, deleteId}) => {

    const handleDetailSearch = (val) => {
        setDetail(val);
        setDetailPage(true);
        setNeedCall(true);
    }

    const handleDetailChange = () => {
        setDetailPage(true);
    }

    // proccess the data to present in the AgGridReact
    const indexRenderer = (props) => {
        return props.value;
    }

    const imageRenderer = (props) => {
        if (props.data["#"] === "") {
            return props.value;
        }

        return (
            <Container style={{display: "flex", justifyContent: "center", alignItems: "center", height: "200px"}}>
                <a href={props.value} target="_blank">
                    <img src={props.value} width="auto" height="150px" />
                </a>
            </Container>
        );
    }

    const titleRenderer = (props) => {
        if (props.data["#"] === "") {
            return props.value;
        }

        return (
            <a title={props.value[0]} className='itemTitle' onClick={() => handleDetailSearch(props.value)} >
                {props.value[0]}
            </a>
        );
    }

    const FavoriteIconRenderer = (val) => {
        if (val.data["#"] === "") {
            return val.value;
        }

        return <FavoriteRenderer 
        props={val.value} 
        wishlistId={wishlistId} 
        setWishlistId={setWishlistId} 
        setDeleteId={setDeleteId}
        deleteId={deleteId} />
    }

    const [columnDefs] = useState([
        { field: "#", width: "60px", cellRenderer: indexRenderer },
        { field: "Image", width: "200px", cellRenderer: imageRenderer },
        { field: "Title", width: "620px", cellRenderer: titleRenderer },
        { field: "Price", width: "120px" },
        { field: "Shipping Option", width: "150px" },
        { field: "Favorite", width: "120px", cellRenderer: FavoriteIconRenderer }
    ]);

    const defaultImage = "https://www.bobswatches.com/rolex-blog/wp-content/uploads/2017/01/ebay-inventory-management-post.jpg";

    const tableData = result.map((item, index) => ({
        "#": index + 1,
        "Image": item.imageUrl || defaultImage,
        "Title": [...item.title, [item.imageUrl ,item.title, item.price, item.cost]],
        "Price": item.price,
        "Shipping Option": item.cost,
        "Favorite": [item.imageUrl ,item.title, item.price, item.cost],
    }));

    const [rowData, setRowData] = useState(tableData);

    const gridOptions = {
        domLayout: 'autoHeight',
    };

    const getRowHeight = (params) => {
        if (params.data["#"] === "") {
            return 50;
        }
        return 200;
    };

    const totalShopping = result.reduce((total, item) => total + parseFloat(item.price.replace('$', '')), 0);

    const totalRow = {
        "#": "",
        "Image": "",
        "Title": "",
        "Price": "",
        "Shipping Option": "Total Shopping",
        "Favorite": '$' + totalShopping.toFixed(2),
    };

    tableData.push(totalRow);

    return (
        tableData.length === 1 ? <AppNoRecords /> : (
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
                    getRowHeight={getRowHeight}
                    gridOptions={gridOptions}
                    rowData={rowData} 
                    columnDefs={columnDefs} />
                </Container>
            </>
        )
    );
};

export default AppWishlist;
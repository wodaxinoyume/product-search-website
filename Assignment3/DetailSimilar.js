import React, { useState } from 'react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import AppNoRecords from './AppNoRecords';
import DetailSimilarCard from './DetailSimilarCard';
import './App.css';

const DetailSimilar = (detailJson3) => {
    if (!detailJson3?.detailJson?.getSimilarItemsResponse?.itemRecommendations?.item) {
        return <AppNoRecords />;
    }

    const items = detailJson3.detailJson.getSimilarItemsResponse.itemRecommendations.item;

    if (items.length === 0) {
        return <AppNoRecords />;
    }

    return <DetailSimilarCard detailJson3={detailJson3} />
}

export default DetailSimilar;
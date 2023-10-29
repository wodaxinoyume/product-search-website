import React, { useEffect, useState, useRef } from 'react';
import { Button } from 'react-bootstrap';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import RemoveShoppingCartIcon from '@mui/icons-material/RemoveShoppingCart';

const handleAdd = (object) => {
    const jsonString = JSON.stringify(object);
    const encodedJsonString = encodeURIComponent(jsonString);

    // const endpoint = `http://localhost:8080/addItem?object=${encodedJsonString}`;
    const endpoint = `https://rugged-shuttle-402803.wn.r.appspot.com/addItem?object=${encodedJsonString}`;

    const url = new URL(endpoint);
    fetch(url)
        .then((response) => {
            if (response.status === 500) {
                throw new Error('Server error');
            }
            return response.json();
        })
        .then((data) => {
            console.log('add succeed');
        })
        .catch((error) => {
            console.error('Error fetching data:', error);
            object.title[0] = encodeURIComponent(object.title[0]);
            // const retryEndpoint = `http://localhost:8080/addItem?object=${encodeURIComponent(JSON.stringify(object))}`;
            const retryEndpoint = `https://rugged-shuttle-402803.wn.r.appspot.com/addItem?object=${encodeURIComponent(JSON.stringify(object))}`;

            const retryUrl = new URL(retryEndpoint);
            fetch(retryUrl)
                .then((response) => {
                    if (response.status === 500) {
                        throw new Error('Server error');
                    }
                    return response.json();
                })
                .then((data) => {
                    console.log('retry add succeed');
                })
                .catch((retryError) => {
                    console.error('Error retrying data:', retryError);
                    object.title[0] = encodeURIComponent("default title");

                    // const retryEndpoint = `http://localhost:8080/addItem?object=${encodeURIComponent(JSON.stringify(object))}`;
                    const reretryEndpoint = `https://rugged-shuttle-402803.wn.r.appspot.com/addItem?object=${encodeURIComponent(JSON.stringify(object))}`;

                    const reretryUrl = new URL(reretryEndpoint);
                    fetch(reretryUrl)
                        .then((response) => response.json())
                        .then((data) => {
                            console.log('reretry add succeed');
                        })
                        .catch((error) => {
                            console.error('Error reretrying data:', error);
                        });
                });
        });
}

const handleDelete = (object) => {

    const jsonString = JSON.stringify(object);
    const encodedJsonString = encodeURIComponent(jsonString);

    // const endpoint = `http://localhost:8080/deleteItem?object=${encodedJsonString}`;
    const endpoint = `https://rugged-shuttle-402803.wn.r.appspot.com/deleteItem?object=${encodedJsonString}`;

    const url = new URL(endpoint);
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            console.log('delete succeed');
        })
        .catch((error) => {
            console.error('Error fetching data:', error);
        });
}

const FavoriteRenderer = ({props, wishlistId, setWishlistId, wishRef}) => {

    const [isFavorite, setIsFavorite] = useState(wishRef.current.includes(parseInt(props[1][1], 10)));

    const toggleFavorite = () => {
        // viewJson();
        setIsFavorite(!isFavorite);
        
        const object = {
            "_id": parseInt(props[1][1], 10),
            "imageUrl": props[0],
            "title": props[1],
            "price": props[2],
            "cost": props[3],
        }

        const deleteObject = {
            "_id": parseInt(props[1][1], 10),
        }
        
        if (isFavorite) {
            handleDelete(deleteObject);
            setWishlistId(wishlistId => wishlistId.filter(item => item !== parseInt(props[1][1], 10)));
            wishRef.current = wishRef.current.filter(item => item !== parseInt(props[1][1], 10));
            console.log(wishRef.current);
        } else {
            handleAdd(object);
            setWishlistId(wishlistId => [...wishlistId, parseInt(props[1][1], 10)]);
            wishRef.current = [...wishRef.current, parseInt(props[1][1], 10)]
            console.log(wishRef.current);
        }
    };

    return (
        <Button variant='light' style={{ marginTop: "10px" }} onClick={toggleFavorite}>
            {isFavorite ? 
            <RemoveShoppingCartIcon style={{color: "rgb(178, 137, 12)"}}/> : 
            <AddShoppingCartIcon />}
        </Button>
    );
};

export default FavoriteRenderer;
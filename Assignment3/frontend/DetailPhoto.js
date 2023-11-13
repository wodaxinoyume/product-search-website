import React from 'react';
import { Card } from 'react-bootstrap';
import './App.css';

const DetailPhoto = (detailJson2) => {
    let images = ["https://rabujoi.files.wordpress.com/2021/12/mt235.jpg?w=840",
                    "https://static1.cbrimages.com/wordpress/wp-content/uploads/2022/03/Eris-Boreas-Greyrat-from-Moshuku-Tensei-Jobless-Reincarnation.jpg",
                    "https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/e26f464d-8fab-46c1-a4eb-a7b647bf6d40/width=450/00137.jpeg",
                    "https://images.goodsmile.info/cgm/images/product/20220421/12602/97889/large/ccd56e5284c5e9ea8d83792604db507f.jpg",
                    "https://m.media-amazon.com/images/I/618OPljgw8L._AC_UF894,1000_QL80_.jpg",
                    "https://resize.cdn.otakumode.com/ex/800.1200/shop/product/aedb477aaf124a51bf64852d2376ae6a.jpg",
                    "https://images.goodsmile.info/cgm/images/product/20220331/12522/97161/large/a6205cd069027a7114fb0c4181b1b211.jpg",
                    "https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/605f5031-bf86-44cd-abec-eeaeca3c66f4/width=450/00136.jpeg"];

    if (detailJson2?.detailJson?.items) {
        const imgArray = detailJson2.detailJson.items.map((item) => item.link);
        images = imgArray;
    }

    return (
        <>
            {detailJson2?.detailJson?.items ? (null) : (
                <Card>
                    <Card.Body className='noRecords'>No Records. Here are some default pictures!</Card.Body>
                </Card>
            )}
            <div style={{display: "flex", justifyContent: "center", flexDirection: "row-reverse", marginTop: "10px"}}>
                <div className='photoDisplay'>
                    {images.map((item, index) => (
                        <div key={index} style={{marginBottom: "10px"}}>
                            <a href={item} target="_target">
                                <img src={item} alt={item} style={{ border: '10px solid #000', width: "100%" }} />
                            </a>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}

export default DetailPhoto;
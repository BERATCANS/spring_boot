import React, { useState, useEffect } from 'react';
import { Form, Row, Col } from 'react-bootstrap';

function StudentSearchBar({ onSearch }) {
    const [query, setQuery] = useState('');

    useEffect(() => {
        const delayDebounce = setTimeout(() => {
            onSearch(query.trim());
        }, 50);
        return () => clearTimeout(delayDebounce);
    }, [query, onSearch]);

    return (
        <Form className="w-100" style={{ maxWidth: '500px' }}>
            <Row className="g-2 align-items-center">
                <Col xs={12}>
                    <Form.Control
                        type="text"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        placeholder="Search..."
                    />
                </Col>
            </Row>
        </Form>
    );
}

export default StudentSearchBar;
